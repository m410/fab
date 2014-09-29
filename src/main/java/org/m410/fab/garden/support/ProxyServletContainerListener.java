package org.m410.fab.garden.support;

import javax.servlet.ServletContext;
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
public final class ProxyServletContainerListener implements ServletContextListener {
    final String applicationClassName;
    final String loaderClassName;
    final File sourceDir;
    final File classesDir;
    final List<URL> runPath;
    ClassLoader classLoader;

    public ProxyServletContainerListener() {
        applicationClassName = null;
        loaderClassName = null;
        sourceDir = null;
        classesDir = null;
        runPath = null;
    }

    public ProxyServletContainerListener(String applicationClassName, String loaderClassName,
            File sourceDir, File classesDir, List<URL> classPathFiles) {
        this.applicationClassName = applicationClassName;
        this.loaderClassName = loaderClassName;
        this.sourceDir = sourceDir;
        this.classesDir = classesDir;
        this.runPath = classPathFiles;
    }


    public ProxyServletContainerListener withApplicationClassName(String name) {
        return new ProxyServletContainerListener(
                name,
                loaderClassName,
                sourceDir,
                classesDir,
                runPath
        );
    }

    public ProxyServletContainerListener withLoaderClassName(String name) {
        return new ProxyServletContainerListener(
                applicationClassName,
                name,
                sourceDir,
                classesDir,
                runPath
        );
    }

    public ProxyServletContainerListener withSourceDir(File dir) {
        return new ProxyServletContainerListener(
                applicationClassName,
                loaderClassName,
                dir,
                classesDir,
                runPath
        );
    }

    public ProxyServletContainerListener withClassesDir(File dir) {
        return new ProxyServletContainerListener(
                applicationClassName,
                loaderClassName,
                sourceDir,
                dir,
                runPath
        );
    }

    public ProxyServletContainerListener withRunPath(List<URL> r) {
        return new ProxyServletContainerListener(
                applicationClassName,
                loaderClassName,
                sourceDir,
                classesDir,
                r
        );
    }

    public ProxyServletContainerListener init() {
        assert  applicationClassName != null;
        assert  loaderClassName != null;
        assert  sourceDir != null;
        assert  classesDir != null;
        assert  runPath != null;

        final ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        classLoader = new LocalDevClassLoader(runPath, classesDir, threadClassLoader);

        // todo remove these later
        System.out.println("---- applicationClassName=" + applicationClassName);
        System.out.println("---- loaderClassName=" + loaderClassName);
        System.out.println("---- sourceDir=" + sourceDir);
        System.out.println("---- classesDir=" + classesDir);
        System.out.println("---- runPath=" + runPath);
        System.out.println("---- classLoader=" + classLoader);

        return this;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("classLoader", classLoader);
        Object application = null;

        try {
            Class appLoaderClass = classLoader.loadClass(loaderClassName);
            Class appClass = classLoader.loadClass(applicationClassName);
            Object appLoader = appLoaderClass.newInstance();
            Method loaderMethod = appLoaderClass.getMethod("load", String.class);
            application = loaderMethod.invoke(appLoader, "dev");  // todo fix hardcoded env

            // todo create and inject code change listener to all proxies

            servletContext.setAttribute("application", application);

            List listServletDef = (List)appClass.getMethod("getServlets").invoke(application);
            Class servletDefClass = classLoader.loadClass("org.m410.garden.servlet.ServletDefinition");
            Class servletClass = classLoader.loadClass("javax.servlet.Servlet");
            Class servletContextClass = classLoader.loadClass("javax.servlet.ServletContext");

            for (Object servletDef : listServletDef)
                    servletDefClass.getMethod("configure",servletContextClass,servletClass)
                            .invoke(servletDef,servletContext, new ProxyServlet());

            List listFilterDef = (List)appClass.getMethod("getServlets").invoke(application);
            Class filterClass = classLoader.loadClass("javax.servlet.Filter");
            Class filterDefClass = classLoader.loadClass("org.m410.garden.servlet.FilterDefinition");

            for (Object filterDef : listFilterDef)
                filterDefClass.getMethod("configure",servletContextClass,filterClass)
                        .invoke(filterDef,servletContext, new ProxyFilter());

            List listListerDef = (List)appClass.getMethod("getListeners").invoke(application);
            Class listenerClass = classLoader.loadClass("java.util.EventListener");
            Class listenerDefClass = classLoader.loadClass("org.m410.garden.servlet.ListenerDefinition");

            for (Object filterDef : listListerDef)
                listenerDefClass.getMethod("configure",servletContextClass,listenerClass)
                        .invoke(filterDef,servletContext, new ProxyListener());

        }
        catch (Exception e) {
            System.err.println("EXCEPTION: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // todo graceful context shutdown...
    }
}