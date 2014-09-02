package org.m410.fab.garden.support;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author m410
 */
public class ProxyServletContainerListener implements ServletContextListener {
    String applicationClassName;
    String loaderClassName;
    File sourceDir;
    File classesDir;
    List<URL> runPath;

    ServletContextListener listener;
    ClassLoader classLoader;

    public ProxyServletContainerListener(String applicationClassName, String loaderClassName,
                                         File sourceDir, File classesDir, List<URL> classPathFiles) {
        this.applicationClassName = applicationClassName;
        this.loaderClassName = loaderClassName;
        this.sourceDir = sourceDir;
        this.classesDir = classesDir;
        this.runPath = classPathFiles;
        final ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        classLoader = new LocalDevClassLoader(runPath, classesDir, threadClassLoader);

        System.out.println("---- applicationClassName=" + applicationClassName);
        System.out.println("---- loaderClassName=" + loaderClassName);
        System.out.println("---- sourceDir=" + sourceDir);
        System.out.println("---- classesDir=" + classesDir);
        System.out.println("---- runPath=" + runPath);
        System.out.println("---- classLoader=" + classLoader);
    }

    private List<File> toRunPath(String runPath) {
        List<String> strs = Arrays.asList(runPath.split(";"));
        List<File> files = new ArrayList<>();

        for (String s : strs)
            files.add(new File(s));

        return files;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("classLoader", classLoader);
        Object application = null;

        try {
            Class appLoaderClass = classLoader.loadClass(loaderClassName);
            Object appLoader = appLoaderClass.newInstance();
            Method loaderMethod = appLoaderClass.getMethod("load", null);
            application = loaderMethod.invoke(appLoader, null);
        } catch (Exception e) {
            System.err.println("EXCEPTION: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // todo, in child loader, might need to be a proxy???
        servletContextEvent.getServletContext().setAttribute("application", application);

        // todo need to add proxies for each of these
//        servletContextEvent.getServletContext().addFilter("","").addMappingForUrlPatterns();
//        servletContextEvent.getServletContext().addServlet("","").addMapping("");
//        servletContextEvent.getServletContext().addListener();

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        new ProxyServletContextEvent(servletContextEvent, classLoader);
    }
}