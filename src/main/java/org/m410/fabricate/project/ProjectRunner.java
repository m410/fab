package org.m410.fabricate.project;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.apache.felix.framework.util.Util;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.m410.fabricate.config.*;
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
    private final String envName;

    public ProjectRunner(List<String> args, String envName, boolean debug) {
        this.args = args;
        this.debug = debug;
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

        try {
            framework.init();
            AutoProcessor.process(osgiRuntimeProps, framework.getBundleContext());
            framework.start();
            BundleContext ctx = framework.getBundleContext();

            final File configFile = ConfigFileUtil.projectConfigFile(System.getProperty("user.dir"));
            final File configCacheDir = ConfigFileUtil.projectConfCache(System.getProperty("user.dir"));

            log(configFile);
            log(configCacheDir);

            Project project = new Project(configFile, configCacheDir, envName);
            project.getBundles().forEach(bundle -> addBundle(ctx, bundle));

            int idx = 0;
            long bundleId = -1;
            for (Bundle bundle : ctx.getBundles()) {
                log(idx +": "+bundle.getBundleId()+", "+bundle.getSymbolicName());
                idx++;

                if(bundle.getSymbolicName().equals("org.m410.fabricate.fab-share")) {
                    bundleId = bundle.getBundleId();
                }
            }

            startBundle(ctx.getBundle(bundleId));

            Arrays.stream(ctx.getBundles())
                    .filter(c->!c.getSymbolicName().equals("org.m410.fabricate.fab-share")) // already started
                    .forEach(this::startBundle);

            Object service = ctx.getService(ctx.getServiceReference("org.m410.fabricate.service.FabricateService"));

            // todo does it need to set commands?

            // todo save to cache.
            // todo this can be simplified a bit, it's sole purpose should be to create a project and cache it.

            service.getClass().getMethod("setEnv", String.class)
                    .invoke(service, envName);
            // might have to pass as string to avoid NoSuchMethodException
            service.getClass().getMethod("addConfig", BaseHierarchicalConfiguration.class)
                    .invoke(service, project.getConfiguration());

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


    private void startBundle(Bundle b) {
        try {
            log("start: " + b);
            b.start();
        } catch (BundleException e) {
            throw new RuntimeException(e);
        }
    }

    void addBundle(final BundleContext ctx, final BundleRef bundleRef) {
        // todo check project file sys cache, if not there put it there

        // todo fix won't work without remote_reference

        bundleRef.getRemoteReference().ifPresent(rr ->{
            String bundlePath = rr.toString();
            boolean present = Arrays.stream(ctx.getBundles())
                    .filter(b -> b.getSymbolicName().equals(bundleRef.getSymbolicName()))
                    .findFirst()
                    .isPresent();

            if (!present) {
                try {
                    log("install:"+bundleRef+", path:"+bundlePath);
                    ctx.installBundle(bundlePath);
                }
                catch (BundleException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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

    private void log(Object msg) {
        if(debug) {
            System.out.println(msg);
        }
    }
}
