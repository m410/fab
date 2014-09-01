package org.m410.fab.garden;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;
import org.m410.fab.garden.web.ProxyServletContainerListener;

import java.io.File;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.List;

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
        final String applicationClass = context.application().getApplicationClass();
        final String appLoaderClass = applicationClass + "Loader";
        final File sourceDir = FileSystems.getDefault().getPath(context.build().getSourceDir()).toFile();
        final File classesDir = FileSystems.getDefault().getPath(context.build().getSourceOutputDir()).toFile();
        final List<URL> classpath = toPath(context.classpaths().get("runtime"));

        final ProxyServletContainerListener listener = new ProxyServletContainerListener(
                applicationClass, appLoaderClass, sourceDir, classesDir, classpath);

        final Server server = new Server(8080);
        final WebAppContext webAppContext = new WebAppContext();

        webAppContext.setResourceBase(new File(context.build().getWebappDir()).getAbsolutePath());
        webAppContext.setContextPath("/");
        webAppContext.setInitParameter("m410-env", context.environment());
        webAppContext.addEventListener(listener);

        server.setHandler(webAppContext);
        server.start();
        server.join();
    }

    private List<URL> toPath(String runtime) {
        return null;
    }
}
