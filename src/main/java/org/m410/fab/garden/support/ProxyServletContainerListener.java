package org.m410.fab.garden.support;

import org.m410.fab.garden.ContextJavaCompiler;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author m410
 */
public final class ProxyServletContainerListener implements ServletContextListener, ReloadingEventListener {
    private final String applicationClassName;
    private final String loaderClassName;
    private final File sourceDir;
    private final File classesDir;
    private final List<URL> runPath;
    private final String compilePath;
    private final String envName;
    private SourceMonitor sourceMonitor;

    private Object application;

    public ProxyServletContainerListener() {
        applicationClassName = null;
        loaderClassName = null;
        sourceDir = null;
        classesDir = null;
        runPath = null;
        compilePath = null;
        envName = null;
    }

    public ProxyServletContainerListener(String applicationClassName, String loaderClassName,
            File sourceDir, File classesDir, List<URL> runPath, String compilePath, String name) {
        this.applicationClassName = applicationClassName;
        this.loaderClassName = loaderClassName;
        this.sourceDir = sourceDir;
        this.classesDir = classesDir;
        this.runPath = runPath;
        this.compilePath = compilePath;
        this.envName = name;
    }


    public ProxyServletContainerListener withApplicationClassName(String name) {
        return new ProxyServletContainerListener(
                name,
                loaderClassName,
                sourceDir,
                classesDir,
                runPath,
                compilePath,
                envName
        );
    }

    public ProxyServletContainerListener withLoaderClassName(String name) {
        return new ProxyServletContainerListener(
                applicationClassName,
                name,
                sourceDir,
                classesDir,
                runPath,
                compilePath,
                envName
        );
    }

    public ProxyServletContainerListener withEnv(String name) {
        return new ProxyServletContainerListener(
                applicationClassName,
                loaderClassName,
                sourceDir,
                classesDir,
                runPath,
                compilePath,
                name
        );
    }

    public ProxyServletContainerListener withSourceDir(File dir) {
        return new ProxyServletContainerListener(
                applicationClassName,
                loaderClassName,
                dir,
                classesDir,
                runPath,
                compilePath,
                envName
        );
    }

    public ProxyServletContainerListener withClassesDir(File dir) {
        return new ProxyServletContainerListener(
                applicationClassName,
                loaderClassName,
                sourceDir,
                dir,
                runPath,
                compilePath,
                envName
        );
    }

    public ProxyServletContainerListener withRunPath(List<URL> r) {
        return new ProxyServletContainerListener(
                applicationClassName,
                loaderClassName,
                sourceDir,
                classesDir,
                r,
                compilePath,
                envName
        );
    }

    public ProxyServletContainerListener withCompilePath(List<URL> r) {
        final String pathSep = System.getProperty("path.separator");
        return new ProxyServletContainerListener(
                applicationClassName,
                loaderClassName,
                sourceDir,
                classesDir,
                runPath,
                r.stream().map(a-> {
                    try{
                        return Paths.get(a.toURI()).toFile().getAbsolutePath();
                    }catch(Exception e){
                        throw new RuntimeException(e);
                    }
                }).reduce("",(a,b)->a + b + pathSep ),
                envName
        );
    }

    public ProxyServletContainerListener init() throws IOException, InterruptedException {
        assert  applicationClassName != null;
        assert  loaderClassName != null;
        assert  sourceDir != null;
        assert  classesDir != null;
        assert  runPath != null;
        assert  compilePath != null;
        assert  envName != null;


        ContextJavaCompiler contextJavaCompiler = new ContextJavaCompiler(sourceDir,classesDir,compilePath);
        AppFactory factory = new AppFactory(runPath,classesDir,applicationClassName, loaderClassName,envName);
        this.sourceMonitor = new SourceMonitor(sourceDir,contextJavaCompiler, factory);

        return this;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final ServletContext servletContext = servletContextEvent.getServletContext();

        try {
            AppRef app = sourceMonitor.getAppFactory().make();
            application = app.getApplication();
            Class appClass = app.getAppClass();
            ClassLoader classLoader = app.getClassLoader();

            servletContext.setAttribute("application", application);

            List listServletDef = (List)appClass.getMethod("getServlets").invoke(application);
            Class servletDefClass = classLoader.loadClass("org.m410.garden.servlet.ServletDefinition");
            Class servletClass = classLoader.loadClass("javax.servlet.Servlet");
            Class servletContextClass = classLoader.loadClass("javax.servlet.ServletContext");

            for (Object servletDef : listServletDef)
                    servletDefClass.getMethod("configure",servletContextClass,servletClass)
                            .invoke(servletDef,servletContext, new ProxyServlet(sourceMonitor));

            List listFilterDef = (List)appClass.getMethod("getFilters").invoke(application);
            Class filterClass = classLoader.loadClass("javax.servlet.Filter");
            Class filterDefClass = classLoader.loadClass("org.m410.garden.servlet.FilterDefinition");

            for (Object filterDef : listFilterDef)
                filterDefClass.getMethod("configure",servletContextClass,filterClass)
                        .invoke(filterDef, servletContext, new ProxyFilter(sourceMonitor));

            List listListerDef = (List)appClass.getMethod("getListeners").invoke(application);
            Class listenerClass = classLoader.loadClass("java.util.EventListener");
            Class listenerDefClass = classLoader.loadClass("org.m410.garden.servlet.ListenerDefinition");

            for (Object listenerDef : listListerDef)
                listenerDefClass.getMethod("configure",servletContextClass,listenerClass)
                        .invoke(listenerDef,servletContext, new ProxyListener(sourceMonitor));

            sourceMonitor.addReloadingListener(this);
            sourceMonitor.fireEvent(new ReloadingEvent(classLoader, application));

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

    private List<URL> toPath(String runtime) {
        return Arrays.asList(runtime.split(System.getProperty("path.separator")))
                .stream().map(s -> {
                    try {
                        return new URL("file://" + s);
                    }
                    catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

    }

    @Override
    public void onChange(ReloadingEvent reloadingEvent) {
        if(reloadingEvent.isRelease()) {
            try {
                this.application.getClass().getMethod("destroy").invoke(this.application);
            }
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            this.application = null;
        }
        else {
            this.application = reloadingEvent.getApplication();
        }
    }
}