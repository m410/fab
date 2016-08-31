package org.m410.fabricate.config;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.m410.config.YamlConfig;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * @author m410
 */
public final class Project implements Reference {
    private final File projectFile;

    private final Archetype archetype;
    private final BuildProperties build;

    private final Reference archetypeReference;
    private final List<Reference> projectEnvReferences;
    private final List<Reference> moduleBaseReferences;
    private final List<Reference> localEnvReferences;
    private final List<String> environments;
    private final Type type;
    private final Level level;

    private final String org;
    private final String name;
    private final String version;
    private final String environment;

    private final File confCache;
    private final BaseHierarchicalConfiguration configuration;
    private final URL url;

    public Project(final File projectFile, final File confCache, final String env) throws IOException, ConfigurationException {
        this.environment = env;
        this.type = Type.PROJECT;
        this.level = Level.PROJECT;

        this.environments = YamlConfig.environments(projectFile);
        this.configuration = YamlConfig.load(projectFile); // the default

        this.org = this.configuration.getString("application.organization");
        this.name = this.configuration.getString("application.name");
        this.version = this.configuration.getString("application.version");

        this.confCache = confCache;
        this.projectFile = projectFile;
        this.url = projectFile.toURI().toURL();

        archetype = loadArchetype(configuration);
        archetypeReference = Resolver.resolveRemote(archetype,  confCache, Collections.singletonList(Resolver.defaultRepo));

        build = loadBuild(configuration, archetypeReference.getConfiguration());

        moduleBaseReferences = loadModules(configuration);
        moduleBaseReferences.addAll(resolveRemote(moduleBaseReferences));

        localEnvReferences = envs(localConfigFile(projectFile), environments); // local envs
        projectEnvReferences = envs(projectFile, environments);
    }

    private Collection<? extends Reference> resolveRemote(List<Reference> moduleBaseReferences) {
        final List<Repository> repositories = Collections.singletonList(Resolver.defaultRepo);

        return moduleBaseReferences.stream()
                .map(m -> Resolver.resolveRemote(m , confCache, repositories))
                .collect(Collectors.toList());
    }

    public URL getUrl() {
        return url;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public Optional<URL> getRemoteReference() {
        return Optional.empty();
    }

    @Override
    public BaseHierarchicalConfiguration getConfiguration() {
        return this.configuration;
    }

    public List<String> getEnvironments() {
        return environments;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getOrg() {
        return org;
    }

    @Override
    public String getEnv() {
        return environment;
    }

    private List<Reference> envs(File file, List<String> envs) {
        List<Reference> localEnvConfs = new ArrayList<>();
        envs.forEach(e-> new ConfigRef(configuration,Type.PROJECT, Level.PROJECT,e));
        return localEnvConfs;
    }

    private File localConfigFile(File projectFile) {
        return new File(projectFile.getAbsolutePath().replace("fab.yml","local.fab.yml"));
    }

    public List<BundleRef> getBundles() throws MalformedURLException {
        List<BundleRef> bundles = new ArrayList<>();
        bundles.addAll(loadBundle(archetypeReference.getConfiguration(), Type.ARCHETYPE, Level.REMOTE)); // load parent first
        bundles.addAll(loadBundle(configuration, Type.PROJECT, Level.PROJECT));

        for (Reference module : moduleBaseReferences) {
            bundles.addAll(loadBundle(module.getConfiguration(), Type.MODULE, Level.REMOTE));
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

    private BuildProperties loadBuild(BaseHierarchicalConfiguration app, BaseHierarchicalConfiguration base) {
        return new BuildProperties(base, app);
    }

    private Archetype loadArchetype(BaseHierarchicalConfiguration configuration) throws MalformedURLException {
        // todo download remote reference
        return new Archetype(configuration.configurationAt("archetype"));
    }

    private List<Reference> loadModules(BaseHierarchicalConfiguration configuration) throws IOException {
        List<Reference> mods = new ArrayList<>();
        mods.addAll(loadModStereotypes("persistence", configuration, Type.PROJECT, Level.PROJECT));
        mods.addAll(loadModStereotypes("modules", configuration, Type.PROJECT, Level.PROJECT));
        mods.addAll(loadModStereotypes("views", configuration, Type.PROJECT, Level.PROJECT));
        mods.addAll(loadModStereotypes("testing", configuration, Type.PROJECT, Level.PROJECT));
        mods.addAll(loadModStereotypes("logging", configuration, Type.PROJECT, Level.PROJECT));

        // todo for each load the remote reference.

        return mods;
    }

    public List<Reference> getModuleBaseReferences() throws MalformedURLException {
        return moduleBaseReferences;
    }

    private List<Reference> loadModStereotypes(String nodeName, BaseHierarchicalConfiguration config, Type type, Level level)  {
        List<Reference> mods = new ArrayList<>();
        final Iterator<String> keys = config.getKeys();
        Pattern modulePattern = Pattern.compile("^("+nodeName+")\\(.*?\\)$");

        while (keys.hasNext()) {
            final String next = keys.next();

            if(modulePattern.matcher(next).matches()) {
                mods.add(new ModuleRef(next, config.configurationAt(next), type, level, environment));
            }
        }

        return mods;
    }


    private List<BundleRef> loadBundle(BaseHierarchicalConfiguration config, Type type, Level level) {
        if(config == null) {
            return new ArrayList<>();
        }
        else {
            return config.configurationsAt("bundles")
                    .stream()
                    .map(c -> new BundleRef(c, type, level, environment))
                    .collect(Collectors.toList());
        }
    }

    public List<Reference> getReferences() {
        List<Reference> configurations = new ArrayList<>();

        configurations.addAll(this.localEnvReferences);
        configurations.addAll(this.projectEnvReferences);
        configurations.add(this);
        moduleBaseReferences.forEach(configurations::add);
        configurations.add(this.archetypeReference);

        return configurations;
    }

    public Reference getArchetypeReference() {
        return archetypeReference;
    }

    @Override
    public String toString() {
        return "ProjectConfig{" +
               "projectFile=" + projectFile +
               ", archetype=" + archetype +
               ", build=" + build +
               ", parent=" + archetypeReference +
               ", modules=" + moduleBaseReferences +
               ", confCache=" + confCache +
               '}';
    }
}
