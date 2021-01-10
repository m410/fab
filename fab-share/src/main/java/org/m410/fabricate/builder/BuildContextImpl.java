package org.m410.fabricate.builder;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.m410.fabricate.config.*;

import java.util.*;
import java.util.stream.StreamSupport;

/**
 * @author m410
 */
public final class BuildContextImpl implements BuildContext {

    private final Cli cli;
    private final Application application;
    private final Build build;
    private final String environment;
    private final List<Dependency> dependencies;
    private final List<BuildModule> modules;
    private final Map<String,String> classpaths;
    private final String hash;
    private final boolean fromCache;
    private final ImmutableHierarchicalConfiguration configuration;

    // for testing
    @Deprecated
    public BuildContextImpl(Cli cli, Application application, Build build, String environment,
                            List<Dependency> dependencies, List<BuildModule> modules) {
        this.cli = cli;
        this.application = application;
        this.build = build;
        this.environment = environment;
        this.dependencies = dependencies;
        this.modules = modules;
        this.fromCache = false;
        this.classpaths = new HashMap<>(); // todo wrong!!!!
        this.hash = null;
        this.configuration = null;
    }

    public BuildContextImpl(Cli cli, ProjectContext context, String hash, String environment) {
        this.hash = hash;
        this.fromCache = false;
        this.cli = cli;
        this.application = context.getApplication();
        this.build = context.getBuild();
        this.environment = environment;
        this.dependencies = context.getDependencies();
        this.modules = context.getModules();
        this.classpaths = new HashMap<>(); // todo wrong!!!!
        this.configuration = context.getConfiguration();
    }

    @Override
    public Optional<ImmutableHierarchicalConfiguration> configAt(String org, String name) {
        final String n = name.replaceAll("\\.", "..");
        final String o = org.replaceAll("\\.", "..");
        final String pattern = "^(\\w+)\\(" + o + ":" + n + ":(.+)\\)$";

        final Iterator<String> keys = configuration.getKeys();
        Iterable<String> iterable = () -> keys;

        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(key -> key.matches(pattern))
                .findFirst()
                .map(configuration::immutableConfigurationAt);
    }

    @Override
    public ImmutableHierarchicalConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public boolean isFromCache() {
        return fromCache;
    }

    @Override
    public String getHash() {
        return hash;
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public Build getBuild() {
        return build;
    }

    @Override
    public Cli cli() {
        return cli;
    }

    @Override
    public Map<String, String> getClasspath() {
        return classpaths;
    }

    @Override
    public String environment() {
        return environment;
    }

    @Override
    public List<Dependency> getDependencies() {
        return dependencies;
    }

    @Override
    public List<BuildModule> getModules() {
        return modules;
    }
}
