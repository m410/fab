package org.m410.fabricate.config;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.m410.config.YamlConfig;
import org.m410.config.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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

    private final File cacheDir;
    private final BaseHierarchicalConfiguration configuration;
    private final URL url;
    private final Resolver resolver;
    private final String checksum;
    private File runtimeConfigFile;
    private List<BundleRef> bundles = new ArrayList<>();


    public Project(final File projectFile, final File cacheDir, final String env) throws IOException,
            ConfigurationException {
        this.environment = env;
        this.type = Type.PROJECT;
        this.level = Level.PROJECT;
        this.checksum = HashUtil.getMD5Checksum(projectFile);

        // todo read cached config & checksum, if hasn't changed, us it.

        this.environments = YamlConfig.environments(projectFile);
        BaseHierarchicalConfiguration projectConfig = YamlConfig.load(projectFile);

        this.org = projectConfig.getString("application.organization");
        this.name = projectConfig.getString("application.name");
        this.version = projectConfig.getString("application.version");

        this.cacheDir = cacheDir;
        this.projectFile = projectFile;
        this.url = projectFile.toURI().toURL();
        this.archetype = new Archetype(projectConfig);

        List<Repository> repositories = new ArrayList<>();
        repositories.addAll(Arrays.asList(Resolver.defaultRepo,Resolver.snapshotRepo)); // should remove this
        repositories.addAll(makeRepositories(projectConfig));
        this.resolver = new Resolver(cacheDir, repositories);

        Reference archetypeReference = resolver.resolveRemote(archetype);

        List<Reference> moduleBaseReferences = loadModules(projectConfig);
        moduleBaseReferences.addAll(resolveRemote(moduleBaseReferences));

        List<Reference> localEnvReferences = envs(localConfigFile(projectFile), environments); // local envs
        List<Reference> projectEnvReferences = envs(projectFile, environments);

        this.configuration = YamlConfigBuilder.builder()
                .withEnv(env)
                .addLocalEnvs(localEnvReferences) // env's
                .addProjectEnvs(projectEnvReferences)  // env's
                .addProject(projectConfig) // default
                .addModules(moduleBaseReferences) //  remote, local & project already loaded
                .addArchetype(archetypeReference) // remote
                .make();

        final Iterator<String> keys = configuration.getKeys();

        while (keys.hasNext()) {
            String next = keys.next();

            if (next.endsWith("bundles")) {
                configuration.configurationsAt(next).forEach(c -> {
                    bundles.add(new BundleRef(c, Type.ARCHETYPE, Level.PROJECT, environment));
                });
            }
        }

        for (BundleRef bundle : bundles) {
            try {
                bundle.setUrl(resolver.resolveBundle(bundle).toURI().toURL());
            }
            catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Collection<? extends Repository> makeRepositories(BaseHierarchicalConfiguration fileConf) {
        return IntStream.range(0, fileConf.getMaxIndex("repositories")+1)
                .mapToObj(i -> new Repository(
                        fileConf.getString("repositories("+i+").id"),
                        fileConf.getString("repositories("+i+").url")
                ))
                .collect(Collectors.toList());
    }

    private Collection<? extends Reference> resolveRemote(List<Reference> moduleBaseReferences) {
        return moduleBaseReferences.stream()
                .map(resolver::resolveRemote)
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

    public List<BundleRef> getBundles() {
        return bundles;
    }

    public BundleRef getBundleByName(String s) {
        return bundles.stream().filter(b -> b.getName().equals(s)).findAny().orElse(null);
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

        // todo replace other module pattern
        Pattern modulePattern = Pattern.compile("^(" + nodeName + ")\\(.*?\\)$");

        while (keys.hasNext()) {
            final String next = keys.next();

            if (modulePattern.matcher(next).matches()) {
                mods.add(new ModuleRef(next, config.configurationAt(next), type, level, environment));
            }
        }

        return mods;
    }

    public File getRuntimeConfigFile() {
        return runtimeConfigFile;
    }

    public void writeToCache() throws IOException, ConfigurationException {
        // todo check hash, if same don't write
        runtimeConfigFile = cacheDir.toPath().resolve(environment + ".yml").toFile();
        runtimeConfigFile.getParentFile().mkdirs();

        try (FileWriter out = new FileWriter(runtimeConfigFile)) {
            ((YamlConfiguration)configuration).write(out);
        }

        final File hash = cacheDir.toPath().resolve(environment + ".yml.md5").toFile();

        try(FileWriter out = new FileWriter(hash)) {
            out.write(this.checksum);
        }
    }

    @Override
    public String toString() {
        return "ProjectConfig{" +
               "projectFile=" + projectFile +
               ", archetype=" + archetype +
               ", modules=" +  // moduleBaseReferences +
               ", confCache=" + cacheDir +
               '}';
    }
}
