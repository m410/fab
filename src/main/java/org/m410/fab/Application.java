package org.m410.fab;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.apache.felix.framework.util.Util;
import org.apache.felix.main.Main;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.*;
import org.apache.felix.main.AutoProcessor;

/**
 * http://felix.apache.org/site/apache-felix-framework-launching-and-embedding.html#ApacheFelixFrameworkLaunchingandEmbedding-customlauncher
 *
 * @author m410
 */
public class Application {
    private static Framework framework = null;

    public static void main(String[] args) throws Exception {

        // (1) Check for command line arguments and verify usage.
        String bundleDir = ".fab/bundles";
        String cacheDir = ".fab/cache";

        // (2) Load system properties.
        Main.loadSystemProperties();

        // (3) Read configuration properties.
        Map<String,String> configProps = loadConfigProperties();

        if (configProps == null) {
            System.err.println("No " + Main.CONFIG_PROPERTIES_FILE_VALUE + " found.");
            configProps = new HashMap<>();
        }

        // (4) Copy framework properties from the system properties.
        Main.copySystemProperties(configProps);
        System.out.println("4");
        // (5) Use the specified auto-deploy directory over default.
        configProps.put(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, bundleDir);
        System.out.println("5");

        // (6) Use the specified bundle cache directory over default.
        configProps.put(Constants.FRAMEWORK_STORAGE, cacheDir);
        System.out.println("6");

        // (7) Add a shutdown hook to clean stop the framework.
        String enableHook = configProps.get(Main.SHUTDOWN_HOOK_PROP);
        System.out.println("7");

        if ((enableHook == null) || !enableHook.equalsIgnoreCase("false"))
            Runtime.getRuntime().addShutdownHook(new Thread("Felix Shutdown Hook") {
                public void run() {
                    try {
                        System.out.println("exit");
                        if (framework != null) {
                            framework.stop();
                            framework.waitForStop(0);
                        }
                    }
                    catch (Exception ex) {
                        System.err.println("Error stopping framework: " + ex);
                    }
                }
            });

        try {
            // (8) Create an instance and initialize the framework.
            FrameworkFactory factory = getFrameworkFactory();
            framework = factory.newFramework(configProps);
            framework.init();
            System.out.println("8");
            // (9) Use the system bundle context to process the auto-deploy
            // and auto-install/auto-start properties.
            AutoProcessor.process(configProps, framework.getBundleContext());
            System.out.println("9");
            // (10) Start the framework.
//            final String s = new File("/Users/m410/Projects/fab(ricate)/fab-loader-bundle" +
//                    "/target/fab-loader-bundle-0.1-SNAPSHOT.jar").toURI().toURL().toString();
            framework.start();
//            framework.getBundleContext().installBundle(s);
            System.out.println("10");
            // (11) Wait for framework to stop to exit the VM.
            framework.waitForStop(0);
            System.out.println("11");
            System.exit(0);
        }
        catch (Exception ex) {
            System.err.println("Could not create framework: " + ex);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private static FrameworkFactory getFrameworkFactory() throws Exception {
        final String name = "META-INF/services/org.osgi.framework.launch.FrameworkFactory";
        java.net.URL url = Main.class.getClassLoader().getResource(name);

        if (url != null) {

            try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {

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

    private static Map<String,String > loadConfigProperties() throws IOException {
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
