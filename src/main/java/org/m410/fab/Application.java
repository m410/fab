package org.m410.fab;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.*;

import org.apache.felix.framework.util.Util;
import org.apache.felix.main.Main;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.*;
import org.apache.felix.main.AutoProcessor;
import org.yaml.snakeyaml.Yaml;

/**
 * http://felix.apache.org/site/apache-felix-framework-launching-and-embedding.html#ApacheFelixFrameworkLaunchingandEmbedding-customlauncher
 *
 * @author m410
 */
public class Application {

    public static void main(String[] args) throws Exception {

        String bundleDir = ".fab/bundles";
        String cacheDir = ".fab/cache";

        System.setProperty("m410.cli.arguments",Arrays.toString(args));

        checkAndSetupProjectDir();

        Main.loadSystemProperties();
        Map<String,String> configProps = loadConfigProperties();

        Main.copySystemProperties(configProps);
        configProps.put(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, bundleDir);
        configProps.put(Constants.FRAMEWORK_STORAGE, cacheDir);

        FrameworkFactory factory = getFrameworkFactory();
        Framework framework = factory.newFramework(configProps);

        try {
            framework.init();
            AutoProcessor.process(configProps, framework.getBundleContext());
            framework.start();

            String fileName = "configuration.m410.yml";
            Map<String, Object> elems = (Map<String, Object>)new Yaml().load(
                    new FileInputStream(new File(fileName)));

            // default lib
            for (Bundle bundle : framework.getBundleContext().getBundles()) {
                System.out.println(" - installed: " + bundle.getSymbolicName() +
                        " : "+  bundle.getVersion());
            }
            framework.getBundleContext().installBundle(
                    new File("/Users/m410/Projects/fab(ricate)/fab-runner-lib/" +
                            "target/fab-lib-0.1-SNAPSHOT.jar").toURI().toURL().toString()
            ).start();

            framework.getBundleContext().installBundle(appBundlePath(fileName, elems)).start();
            List modules = (List)elems.get("modules");
            final List persistence = (List) elems.get("persistence");

            if(persistence != null)
                modules.addAll(persistence);

            final List view = (List) elems.get("view");

            if(view != null)
                modules.addAll(view);

            for (Object module : modules)
                framework.getBundleContext().installBundle(moduleBundlePath((Map<String,Object>)module)).start();

        } finally {
            framework.stop();
        }
    }

    private static void checkAndSetupProjectDir() throws IOException{
        final File localCacheDir = FileSystems.getDefault().getPath(".fab").toFile();

        if(!localCacheDir.exists()) {
            localCacheDir.mkdir();
            new File(localCacheDir,"bundles").mkdir();
            new File(localCacheDir,"cache").mkdir();
            File confFile = new File(localCacheDir,"config.properties");

            try (FileWriter w = new FileWriter(confFile)) {
                w.write("obr.repository.url=http://felix.apache.org/obr/releases.xml");
            }
        }
    }

    static String appBundlePath(String fileName, Map<String,Object> yaml) throws MalformedURLException {
        final String s = new File("/Users/m410/Projects/fab(ricate)/fab-loader-bundle/" +
                "target/fab-loader-0.1-SNAPSHOT.jar").toURI().toURL().toString();
        System.out.println("-- add app: " + s);
        return s;
    }

    static String moduleBundlePath(Map<String,Object> yaml) throws MalformedURLException {
        final String s = new File("/Users/m410/Projects/fab(ricate)/fab-java-task-bundle/" +
                "target/fab-java-task-0.1-SNAPSHOT.jar").toURI().toURL().toString();
        System.out.println("-- add module: " + s);
        return s;
    }

    static FrameworkFactory getFrameworkFactory() throws Exception {
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
