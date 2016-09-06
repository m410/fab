package org.m410.fabricate.config;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
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
 * This is the heart of of the CLI, it creates a project from by assembling all the configuration files into
 * one merged configuration.
 *
 * @author m410
 */
public final class Project implements Reference {
    private final Pattern modulePattern = Pattern.compile("^(persistence|modules|views|testing|logging)\\(.*?\\)$");

    private final File projectFile;

    private final Archetype archetype;

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

    public Project(final File projectFile, final File confCache, final String env) throws IOException,
            ConfigurationException {
        this.environment = env;
        this.type = Type.PROJECT;
        this.level = Level.PROJECT;

        this.environments = YamlConfig.environments(projectFile);
        BaseHierarchicalConfiguration fileConf = YamlConfig.load(projectFile);

        this.org = fileConf.getString("application.organization");
        this.name = fileConf.getString("application.name");
        this.version = fileConf.getString("application.version");

        this.confCache = confCache;
        this.projectFile = projectFile;
        this.url = projectFile.toURI().toURL();

        archetype = new Archetype(fileConf);

        // todo pull repositories from config
        final List<Repository> repos = Collections.singletonList(Resolver.defaultRepo);
        Reference archetypeReference = Resolver.resolveRemote(archetype, confCache, repos);

        List<Reference> moduleBaseReferences = loadModules(fileConf);
        moduleBaseReferences.addAll(resolveRemote(moduleBaseReferences));

        List<Reference> localEnvReferences = envs(localConfigFile(projectFile), environments); // local envs
        List<Reference> projectEnvReferences = envs(projectFile, environments);

        this.configuration = YamlConfigBuilder.builder()
                .withEnv(env)
                .addLocalEnvs(localEnvReferences) // env's
                .addProjectEnvs(projectEnvReferences)  // env's
                .addProject(fileConf) // default
                .addModules(moduleBaseReferences) //  remote, local & project already loaded
                .addArchetype(archetypeReference) // remote
                .make();
    }

    private Collection<? extends Reference> resolveRemote(List<Reference> moduleBaseReferences) {
        final List<Repository> repositories = Collections.singletonList(Resolver.defaultRepo);

        return moduleBaseReferences.stream()
                .map(m -> Resolver.resolveRemote(m, confCache, repositories))
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
        return envs.stream()
                .map(e -> toConfigRef(file, e))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(c -> !c.getEnv().equals("default"))
                .filter(c -> c.getName() != null)
                .collect(Collectors.toList());
    }

    private Optional<ConfigRef> toConfigRef(File file, String e) {

        if (!file.exists()) {
            return Optional.empty();
        }

        try {
            final BaseHierarchicalConfiguration load = YamlConfig.load(file, e);
            return Optional.of(new ConfigRef(load, Type.PROJECT, Level.PROJECT, e));
        }
        catch (ConfigurationException e1) {
            throw new RuntimeException(e1);
        }
    }

    private File localConfigFile(File projectFile) {
        return new File(projectFile.getAbsolutePath().replace("fab.yml", "local.fab.yml"));
    }

    public List<? extends Reference> getModuleReferences() {
        List<ModuleRef> modules = new ArrayList<>();

        final Iterator<String> keys = configuration.getKeys();

        while (keys.hasNext()) {
            String next = keys.next();

            if (modulePattern.matcher(next).matches()) {
                configuration.configurationsAt(next).forEach(c -> {
                    modules.add(new ModuleRef(next, c, Type.PROJECT, Level.PROJECT, environment));
                });
            }
        }

        return modules;
    }

    public List<BundleRef> getBundles() throws MalformedURLException {
        List<BundleRef> bundles = new ArrayList<>();
        final Iterator<String> keys = configuration.getKeys();

        while (keys.hasNext()) {
            String next = keys.next();

            if (next.endsWith("bundles")) {
                configuration.configurationsAt(next).forEach(c -> {
                    bundles.add(new BundleRef(c, Type.ARCHETYPE, Level.PROJECT, environment));
                });
            }
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
        return new BuildProperties(configuration);
    }

    private List<Reference> loadModules(BaseHierarchicalConfiguration configuration) throws IOException {
        // todo this could be cleaned up by using the modulePattern regex
        List<Reference> mods = new ArrayList<>();
        mods.addAll(loadModStereotypes("persistence", configuration, Type.PROJECT, Level.PROJECT));
        mods.addAll(loadModStereotypes("modules", configuration, Type.PROJECT, Level.PROJECT));
        mods.addAll(loadModStereotypes("views", configuration, Type.PROJECT, Level.PROJECT));
        mods.addAll(loadModStereotypes("testing", configuration, Type.PROJECT, Level.PROJECT));
        mods.addAll(loadModStereotypes("logging", configuration, Type.PROJECT, Level.PROJECT));

        return mods;
    }

    private List<Reference> loadModStereotypes(String nodeName, BaseHierarchicalConfiguration config, Type type,
            Level level) {
        List<Reference> mods = new ArrayList<>();
        final Iterator<String> keys = config.getKeys();
        Pattern modulePattern = Pattern.compile("^(" + nodeName + ")\\(.*?\\)$");

        while (keys.hasNext()) {
            final String next = keys.next();

            if (modulePattern.matcher(next).matches()) {
                mods.add(new ModuleRef(next, config.configurationAt(next), type, level, environment));
            }
        }

        return mods;
    }

    @Override
    public String toString() {
        return "ProjectConfig{" +
               "projectFile=" + projectFile +
               ", archetype=" + archetype +
               ", modules=" +  // moduleBaseReferences +
               ", confCache=" + confCache +
               '}';
    }

}
