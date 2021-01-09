package org.m410.fabricate.project;

import org.apache.felix.framework.util.Util;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.m410.fabricate.config.BundleRef;
import org.m410.fabricate.config.ConfigFileUtil;
import org.m410.fabricate.config.Project;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;

/**
 * @author m410
 */
public final class ProjectRunner {

    // todo should be pulled from config
    static final String bundleDir = ".fab/bundles";
    static final String cacheDir = ".fab/cache";
    private final List<String> args;
    private final String envName;
    private final String outputLevel;

    public ProjectRunner(List<String> args, String envName, String logLevel) {
        this.args = args;
        this.outputLevel = logLevel;
        this.envName = envName;
    }

    public void run() throws Throwable {
        checkAndSetupProjectDir();
        Main.loadSystemProperties();
        Map<String, String> osgiRuntimeProps = loadConfigProperties();
        Main.copySystemProperties(osgiRuntimeProps);
        osgiRuntimeProps.put(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, bundleDir);
        osgiRuntimeProps.put(Constants.FRAMEWORK_STORAGE, cacheDir);

        FrameworkFactory factory = getFrameworkFactory();
        Framework framework = factory.newFramework(osgiRuntimeProps);

        final File configFile = ConfigFileUtil.projectConfigFile(System.getProperty("user.dir"));
        final File configCacheDir = ConfigFileUtil.projectConfCache(System.getProperty("user.dir"));

        log(configFile);
        log(configCacheDir);

        try {
            framework.init();
            AutoProcessor.process(osgiRuntimeProps, framework.getBundleContext());
            framework.start();
            BundleContext ctx = framework.getBundleContext();

            Project project = new Project(configFile, configCacheDir, envName);
            project.writeToCache();

            // add the bundles, load felix & fab-share first
            addBundle(ctx, project.getBundleByName("fab-share"));

            project.getBundles().stream()
                    .filter(c -> !c.getSymbolicName().equals("org.m410.fabricate.fab-share"))
                    .forEach(bundle -> addBundle(ctx, bundle));

            // start the bundles, load felix & fab-share first
            startBundle(ctx.getBundle(findIdByName(ctx, "org.apache.felix.framework")));
            startBundle(ctx.getBundle(findIdByName(ctx, "org.m410.fabricate.fab-share")));

            Arrays.stream(ctx.getBundles())
                    .filter(c -> !c.getSymbolicName().equals("org.apache.felix.framework"))
                    .filter(c -> !c.getSymbolicName().equals("org.m410.fabricate.fab-share"))
                    .forEach(this::startBundle);

            // start the service
            Object service = ctx.getService(ctx.getServiceReference("org.m410.fabricate.service.FabricateService"));
            service.getClass().getMethod("setEnv", String.class).invoke(service, envName);
            service.getClass().getMethod("setOutputLevel", String.class).invoke(service, outputLevel);
            
            String out = String.join("\n", Files.readAllLines(project.getRuntimeConfigFile().toPath()));
            service.getClass().getMethod("addConfig", String.class).invoke(service, out);

            service.getClass().getMethod("postStartupWiring").invoke(service);

            final String[] objects = args.toArray(new String[args.size()]);
            service.getClass().getMethod("execute", String[].class).invoke(service, new Object[]{objects});
        }
        catch (InvocationTargetException e) {
            throw e.getTargetException(); // just throw the root cause
        }
        finally {
            framework.stop();
        }
    }

    private long findIdByName(BundleContext ctx, String name) {
        long bundleId = -1;

        for (Bundle bundle : ctx.getBundles()) {
            if (bundle.getSymbolicName().equals(name)) {
                bundleId = bundle.getBundleId();
            }
        }

        return bundleId;
    }


    private void startBundle(Bundle b) {
        try {
            log("start: " + b);
            b.start();
        }
        catch (BundleException e) {
            throw new RuntimeException(e);
        }
    }

    void addBundle(final BundleContext ctx, final BundleRef bundleRef) {

        try {
            ctx.installBundle(bundleRef.getSymbolicName(), bundleRef.getUrl().openStream());
        }
        catch (BundleException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkAndSetupProjectDir() throws IOException {
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

    private FrameworkFactory getFrameworkFactory() throws Exception {
        final String name = "META-INF/services/org.osgi.framework.launch.FrameworkFactory";
        java.net.URL url = Main.class.getClassLoader().getResource(name);

        if (url != null) {

            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {

                for (String s = br.readLine(); s != null; s = br.readLine()) {
                    s = s.trim();
                    // Try to load first non-empty, non-commented line.

                    if ((s.length() > 0) && (s.charAt(0) != '#')) {
                        return (FrameworkFactory) Class.forName(s).newInstance();
                    }
                }
            }
        }

        throw new Exception("Could not find framework factory.");
    }

    private Map<String, String> loadConfigProperties() throws IOException {
        Properties props = new Properties();
        final File file = new File(new File(".fab"), Main.CONFIG_PROPERTIES_FILE_VALUE);

        try (FileInputStream is = new FileInputStream(file)) {
            props.load(is);
        }

        // Perform variable substitution for system properties and convert to dictionary.
        Map<String, String> map = new HashMap<>();
        for (Enumeration e = props.propertyNames(); e.hasMoreElements(); ) {
            String name = (String) e.nextElement();
            map.put(name, Util.substVars(props.getProperty(name), name, null, props));
        }

        return map;
    }

    private void log(Object msg) {
        if (outputLevel.equals("debug")) {
            System.out.println(msg);
        }
    }
}
