package org.m410.fabricate.project;

import org.apache.felix.framework.util.Util;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.m410.fabricate.config.BundleRef;
import org.m410.fabricate.config.ConfigUtil;
import org.m410.fabricate.config.ProjectConfig;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.*;

/**
 * @author m410
 */
public final class ProjectRunner {

    static final String bundleDir = ".fab/bundles";
    static final String cacheDir = ".fab/cache";
    private final List<String> args;
    private final boolean debug;

    public ProjectRunner(List<String> args, boolean debug) {
        this.args = args;
        this.debug = debug;
    }

    public void run() throws Throwable {
        checkAndSetupProjectDir();
        Main.loadSystemProperties();
        Map<String, String> configProps = loadConfigProperties();
        Main.copySystemProperties(configProps);
        configProps.put(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, bundleDir);
        configProps.put(Constants.FRAMEWORK_STORAGE, cacheDir);

        FrameworkFactory factory = getFrameworkFactory();
        Framework framework = factory.newFramework(configProps);

        try {
            framework.init();
            AutoProcessor.process(configProps, framework.getBundleContext());
            framework.start();
            BundleContext ctx = framework.getBundleContext();

            final File configFile = ConfigUtil.projectConfigFile(System.getProperty("user.dir"));
            final File configCacheDir = ConfigUtil.projectConfCache(System.getProperty("user.dir"));
            ProjectConfig config = new ProjectConfig(configFile, configCacheDir);

            config.getBundles().stream()
                    .sorted((a, b) -> a.getName().equals("fab-share") ? 0 : 1)
                    .forEach(s -> addBundle(ctx, s));
            Arrays.asList(ctx.getBundles()).stream().forEach(this::startBundle);

            Object buildService = ctx.getService(ctx.getServiceReference("org.m410.fabricate.service.FabricateService"));

            // load each full configuration
            // todo might want to add each type of configuration, so the service can sort as needed
            for (Map<String, Object> conf : config.getConfigurations())
                buildService.getClass().getMethod("addConfig", Map.class).invoke(buildService, conf);

            buildService.getClass().getMethod("postStartupWiring").invoke(buildService);
            final String[] objects = args.toArray(new String[args.size()]);
            buildService.getClass().getMethod("execute", String[].class).invoke(buildService, new Object[]{objects});
        }
        catch (InvocationTargetException e) {
            // just throw the root cause
            throw e.getTargetException();
        }
        finally {
            framework.stop();
        }
    }


    private void startBundle(Bundle b) {
        try {
            b.start();
        } catch (BundleException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addBundle(BundleContext ctx, BundleRef s) {
        // todo check project file sys cache, if not there put it there
        try {
            final String bundlePath = s.makeUrl().toString();
            final boolean present = Arrays.asList(ctx.getBundles()).stream()
                    .filter(b -> b.getSymbolicName().equals(s.getSymbolicName()))
                    .findFirst()
                    .isPresent();

            if (!present) {
                ctx.installBundle(bundlePath);
            }
        }
        catch (Exception e) {
            throw new InvalidBundleRefException(e,s);
        }
    }

    void checkAndSetupProjectDir() throws IOException {
        final File localCacheDir = FileSystems.getDefault().getPath(".fab").toFile();

        if (!localCacheDir.exists()) {
            localCacheDir.mkdir();
            new File(localCacheDir, "bundles").mkdir();
            new File(localCacheDir, "cache").mkdir();
            File confFile = new File(localCacheDir, "config.properties");

            try (FileWriter w = new FileWriter(confFile)) {
                w.write("obr.repository.url=http://felix.apache.org/obr/releases.xml");
            }
        }
    }

    FrameworkFactory getFrameworkFactory() throws Exception {
        final String name = "META-INF/services/org.osgi.framework.launch.FrameworkFactory";
        java.net.URL url = Main.class.getClassLoader().getResource(name);

        if (url != null) {

            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {

                for (String s = br.readLine(); s != null; s = br.readLine()) {
                    s = s.trim();
                    // Try to load first non-empty, non-commented line.

                    if ((s.length() > 0) && (s.charAt(0) != '#'))
                        return (FrameworkFactory) Class.forName(s).newInstance();
                }
            }
        }

        throw new Exception("Could not find framework factory.");
    }

    private Map<String, String> loadConfigProperties() throws IOException {
        Properties props = new Properties();
        URL propURL = new File(new File(".fab"), Main.CONFIG_PROPERTIES_FILE_VALUE).toURI().toURL();
        InputStream is = propURL.openConnection().getInputStream();
        props.load(is);
        is.close();

        // Perform variable substitution for system properties and convert to dictionary.
        Map<String, String> map = new HashMap<>();
        for (Enumeration e = props.propertyNames(); e.hasMoreElements(); ) {
            String name = (String) e.nextElement();
            map.put(name, Util.substVars(props.getProperty(name), name, null, props));
        }

        return map;

    }
}
