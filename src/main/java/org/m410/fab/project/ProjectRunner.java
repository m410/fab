package org.m410.fab.project;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.felix.framework.util.Util;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author m410
 */
public final class ProjectRunner {

    static final String bundleDir = ".fab/bundles";
    static final String cacheDir = ".fab/cache";
    private final List<String> args;

    public ProjectRunner(List<String> args) {
        this.args = args;
    }

    public void run() throws Throwable {
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
            BundleContext ctx = framework.getBundleContext();

            final File configFile = projectConfigFile();
            BuildConfig localConfig = loadLocalConfig(configFile);
            Set<String> violations = localConfig.verifyResources();

            if(violations.size() > 0) {
                System.out.println("got config errors");
            }
            else {
                localConfig.resources().stream().forEach(s -> addBundle(ctx, s));
                Arrays.asList(ctx.getBundles()).stream().forEach(this::startBundle);
                Object buildService = ctx.getService(ctx.getServiceReference("org.m410.fab.service.FabricateService"));

                try {
                    buildService.getClass().getMethod("postStartupWiring").invoke(buildService);
                    System.out.println("  ## args: " + args);
                    final String[] objects = args.toArray(new String[args.size()]);
                    buildService.getClass().getMethod("execute",String[].class).invoke(buildService, new Object[]{objects});

                } catch (InvocationTargetException e) {
                    // just throw the root cause
                    throw e.getTargetException();
                }
            }
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
        try {
            final String bundlePath = s.makeUrl().toString();
            final boolean present = Arrays.asList(ctx.getBundles()).stream().filter(b ->
                            b.getSymbolicName().equals(s.getSymbolicName())
            ).findFirst().isPresent();


            if(!present) {
                System.out.println(" ## loading: " + s.getSymbolicName());
                ctx.installBundle(bundlePath);
            }
        }
        catch (BundleException e) {
            throw new RuntimeException(e);
        }
    }

    private static File projectConfigFile() {
        return new File("garden.fab.yml");
    }

    @SuppressWarnings("unchecked")
    BuildConfig loadLocalConfig(File configFile) throws Exception {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        final Constructor constructor = new Constructor(BuildConfig.class);
        TypeDescription buildConfigTypeDef = new TypeDescription(BuildConfig.class);
        buildConfigTypeDef.putListPropertyType("bundles", BundleRef.class);
        buildConfigTypeDef.putListPropertyType("persistence", BundleRef.class);
        buildConfigTypeDef.putListPropertyType("modules", BundleRef.class);
        buildConfigTypeDef.putListPropertyType("view", BundleRef.class);
        constructor.addTypeDescription(buildConfigTypeDef);

        BuildConfig bean =  (BuildConfig)new Yaml(constructor,representer)
                .load(new FileInputStream(configFile));
        bean.merge(loadBaseConfig(bean));
        return bean;
    }


    @SuppressWarnings("unchecked")
    BaseConfig loadBaseConfig(BuildConfig local) throws Exception {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        final Constructor constructor = new Constructor(BaseConfig.class);
        TypeDescription buildConfigTypeDef = new TypeDescription(BaseConfig.class);
        buildConfigTypeDef.putListPropertyType("bundles", BundleRef.class);
        constructor.addTypeDescription(buildConfigTypeDef);
        return (BaseConfig)new Yaml(constructor,representer)
                .load(local.makeUrl().openStream());
    }

    @SuppressWarnings("unchecked")
    List<BundleRef> collectBundles(Object arg) {
        if(arg != null && arg instanceof List)
            return ((List<Map<String,Object>>)arg).stream().map(m ->{
                BundleRef b = new BundleRef();

                try {
                    BeanUtils.populate(b,m);
                } catch (IllegalAccessException  | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                return b;
            }).collect(Collectors.toList());
        else
            return new ArrayList<>();
    }

    void checkAndSetupProjectDir() throws IOException {
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

    FrameworkFactory getFrameworkFactory() throws Exception {
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

    private Map<String,String > loadConfigProperties() throws IOException {
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
