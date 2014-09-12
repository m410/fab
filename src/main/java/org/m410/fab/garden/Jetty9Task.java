package org.m410.fab.garden;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;
import org.m410.fab.garden.support.ProxyServletContainerListener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author m410
 */
public class Jetty9Task implements Task {
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
        System.out.println("context:"+context);
        System.out.println("context.app:"+context.getApplication());
        System.out.println("context.build:"+context.getBuild());
        System.out.println("context.classpath:"+context.getClasspath());
        final String applicationClass = context.getApplication().getApplicationClass();
        final String appLoaderClass = applicationClass + "Loader";
        final File sourceDir = FileSystems.getDefault().getPath(context.getBuild().getSourceDir()).toFile();
        final File classesDir = FileSystems.getDefault().getPath(context.getBuild().getSourceOutputDir()).toFile();
        final List<URL> classpath = toPath(context.getClasspath().get("compile")); // was runtime

        final ProxyServletContainerListener listener = new ProxyServletContainerListener(
                applicationClass, appLoaderClass, sourceDir, classesDir, classpath);

        System.out.println("got new listeners:" + listener);

        final Server server = new Server(8080);
        final WebAppContext webAppContext = new WebAppContext();

        webAppContext.setResourceBase(new File(context.getBuild().getWebappDir()).getAbsolutePath());
        webAppContext.setContextPath("/");
        webAppContext.setInitParameter("m410-env", context.environment());
        webAppContext.addEventListener(listener);

        server.setHandler(webAppContext);
        server.start();
        server.join();
    }

    private List<URL> toPath(String runtime) {
        return Arrays.asList(runtime.split(System.getProperty("path.separator")))
                .stream().map(s -> {
                    try {
                        return new URL("file:/" + s);
                    }
                    catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

    }
}
