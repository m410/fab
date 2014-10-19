package org.m410.fabricate.config;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author m410
 */
public final class ProjectConfig extends Base {
    private File projectFile;

    private Archetype archetype;
    private BuildProperties build;

    private ConfigRef parent;
    private List<ConfigRef> modules;

    private File confCache;


    public ProjectConfig(Map<String, Object> configuration, File confCache) throws IOException {
        this.configuration = configuration;
        this.confCache = confCache;
        archetype = loadArchetype(configuration);
        parent = loadParent(configuration);
        build = loadBuild(configuration, parent.configuration);
        modules = loadModules(configuration);
    }

    @SuppressWarnings("unchecked")
    public ProjectConfig(File projectFile, File confCache) throws IOException {
        this((Map<String,Object>) new Yaml().load(new FileInputStream(projectFile)),confCache);
        this.projectFile = projectFile;
        this.url = projectFile.toURI().toURL();
    }

    @Override
    public List<BundleRef> getBundles() throws MalformedURLException {
        List<BundleRef> bundles = new ArrayList<>();
        loadBundle(bundles,parent.configuration); // load parent first
        loadBundle(bundles,configuration);

        for (ConfigRef module : modules) {
            loadBundle(bundles,module.configuration);
        }

        return bundles;
    }

    public File getProjectFile() {
        return projectFile;
    }

    public Archetype getArchetype() {
        return archetype;
    }

    public BuildProperties getBuild() {
        return build;
    }

    private BuildProperties loadBuild(Map<String, Object> app, Map<String, Object> base) {
        return new BuildProperties(base).with(app);
    }

    @SuppressWarnings("unchecked")
    private Archetype loadArchetype(Map<String, Object> configuration) throws MalformedURLException {
        return new Archetype((Map<String,String>)configuration.get("archetype"));
    }

    private List<ConfigRef> loadModules(Map<String, Object> configuration) throws IOException {
        List<ConfigRef> mods = new ArrayList<>();
        loadMod("persistence", mods, configuration);
        loadMod("modules", mods, configuration);
        loadMod("view", mods, configuration);
//        loadMod("test", mods, configuration);
//        loadMod("logging", mods, configuration);
        return mods;
    }

    @SuppressWarnings("unchecked")
    private ConfigRef loadParent(Map<String, Object> configuration) throws IOException {
        Map<String,String> archetype = (Map<String,String>)configuration.get("archetype");
        return new ConfigRef(makeUrl(archetype,confCache,null));
    }

    public List<ConfigRef> getModules() throws MalformedURLException {
        return modules;
    }

    @SuppressWarnings("unchecked")
    private void loadMod(String nodeName, List<ConfigRef> modules, Map<String, Object> config)
            throws IOException {
        if(config.containsKey(nodeName)) {
            final Object child = config.get(nodeName);

            if(child instanceof List){
                final List<Map<String,String>> list = (List<Map<String,String>>) child;

                for (Map<String,String> persistence : list)
                    modules.add(new ConfigRef(makeUrl(persistence,confCache,null)));
            }
            else {
                modules.add(new ConfigRef(makeUrl((Map<String,String>)child,confCache,null)));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadBundle(List<BundleRef> modules, Map<String, Object> config) throws MalformedURLException {
        final String nodeName = "bundles";

        if(config.containsKey(nodeName)) {
            final Object child = config.get(nodeName);

            if(child instanceof List){
                final List<Map<String,String>> list = (List<Map<String,String>>) child;

                for (Map<String,String> persistence : list)
                    modules.add(new BundleRef(persistence));
            }
            else {
                modules.add(new BundleRef((Map<String,String>)child));
            }
        }
    }

    public List<Map<String, Object>> getConfigurations() {
        List<Map<String, Object>> configurations = new ArrayList<>();
        configurations.add(this.configuration);
        configurations.add(this.parent.configuration);
        modules.stream().forEach(c->configurations.add(c.configuration));
        return configurations;
    }

    public ConfigRef getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "ProjectConfig{" +
                "projectFile=" + projectFile +
                ", archetype=" + archetype +
                ", build=" + build +
                ", parent=" + parent +
                ", modules=" + modules +
                ", confCache=" + confCache +
                '}';
    }
}
