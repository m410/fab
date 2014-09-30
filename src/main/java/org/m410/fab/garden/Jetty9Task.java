package org.m410.fab.garden;

//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.webapp.WebAppContext;
import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;
import org.m410.fab.garden.support.ProxyServletContainerListener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author m410
 */
public class Jetty9Task implements Task {

    String applicationClass;
    String appLoaderClass;
    File sourceDir;
    File classesDir;
    List<URL> classpath;

    @Override
    public String getName() {
        return "jetty9";
    }

    @Override
    public String getDescription() {
        return "Jetty9 reloading server";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        context.cli().debug("context:"+context);
        context.cli().debug("context.app:"+context.getApplication());
        context.cli().debug("context.build:"+context.getBuild());
        context.cli().debug("context.classpath:"+context.getClasspath());

        applicationClass = context.getApplication().getApplicationClass();
        appLoaderClass = applicationClass + "Loader";
        sourceDir = FileSystems.getDefault().getPath(context.getBuild().getSourceDir()).toFile();
        classesDir = FileSystems.getDefault().getPath(context.getBuild().getSourceOutputDir()).toFile();
        classpath = toPath(context.getClasspath().get("compile")); // was runtime

        // works, but needs to be loaded dynamically from another group of dependencies
        List<URL> jettyClasspath = new ArrayList<>();
        jettyClasspath.add(new File("/Users/m410/.m2/repository/org/eclipse/jetty/aggregate/jetty-all/9.2.3.v20140905/jetty-all-9.2.3.v20140905.jar").toURI().toURL());
        jettyClasspath.add(new File("/Users/m410/.m2/repository/javax/websocket/javax.websocket-api/1.1/javax.websocket-api-1.1.jar").toURI().toURL());
        jettyClasspath.add(new File("/Users/m410/.m2/repository/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar").toURI().toURL());
        jettyClasspath.add(new File("/Users/m410/.m2/repository/org/m410/fabricate/garden/fab-java-garden-project/0.1-SNAPSHOT/fab-java-garden-project-0.1-SNAPSHOT.jar").toURI().toURL());

        new DeligateApp(jettyClasspath, context.getBuild().getWebappDir(), context.environment());
    }

    class DeligateApp implements Runnable {
        URLClassLoader loader;
        String webappDirPath;
        String env;

        DeligateApp(List<URL> urls, String webappDirPath, String env) throws InterruptedException {
            this.webappDirPath = webappDirPath;
            this.env = env;
            this.loader = new URLClassLoader(urls.toArray(new URL[urls.size()]),null);
            final Thread thread = new Thread(this);
            thread.setContextClassLoader(loader);
            thread.start();
            thread.join();
        }

        @Override @SuppressWarnings("unchecked") public void run() {
            try {
                Class serverClass = loader.loadClass("org.eclipse.jetty.server.Server");
                Object server = serverClass.getConstructor(Integer.TYPE).newInstance(8080);

                Class webAppContextClass = loader.loadClass("org.eclipse.jetty.webapp.WebAppContext");
                Object webAppContext = webAppContextClass.newInstance();

                String path = new File(webappDirPath).getAbsolutePath();
                webAppContextClass.getMethod("setResourceBase",String.class).invoke(webAppContext,path);
                webAppContextClass.getMethod("setContextPath",String.class).invoke(webAppContext,"/");
                webAppContextClass.getMethod("setInitParameter",String.class,String.class).invoke(webAppContext,"m410-env",env);

                Class proxyClass = loader.loadClass("org.m410.fab.garden.support.ProxyServletContainerListener");
                Object listener = proxyClass.newInstance();
                listener = proxyClass.getMethod("withApplicationClassName",String.class).invoke(listener,applicationClass);
                listener = proxyClass.getMethod("withLoaderClassName",String.class).invoke(listener,appLoaderClass);
                listener = proxyClass.getMethod("withSourceDir",File.class).invoke(listener,sourceDir);
                listener = proxyClass.getMethod("withClassesDir",File.class).invoke(listener,classesDir);
                listener = proxyClass.getMethod("withRunPath",List.class).invoke(listener,classpath);
                listener = proxyClass.getMethod("init").invoke(listener);


                Class listenerClass = loader.loadClass("java.util.EventListener");
                webAppContextClass.getMethod("addEventListener",listenerClass).invoke(webAppContext,listener);

                Class handlerClass = loader.loadClass("org.eclipse.jetty.server.Handler");
                serverClass.getMethod("setHandler",handlerClass).invoke(server,webAppContext);
                serverClass.getMethod("start").invoke(server);
                serverClass.getMethod("join").invoke(server);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
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
}
