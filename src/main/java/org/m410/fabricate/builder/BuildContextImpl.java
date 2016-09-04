package org.m410.fabricate.builder;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.m410.fabricate.config.*;
import org.m410.fabricate.service.internal.serialize.CachedProject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author m410
 */
public final class BuildContextImpl implements BuildContext {
    private final Cli cli;
    private final Application application;
    private final Build build;
    private final String environment;
    private final List<Dependency> dependencies;
    private final List<Module> modules;
    private final Map<String,String> classpaths;
    private final String hash;
    private final boolean fromCache;
    private final ImmutableHierarchicalConfiguration configuration;

    // for testing
    public BuildContextImpl(Cli cli, Application application, Build build, String environment,
                            List<Dependency> dependencies, List<Module> modules) {
        this.cli = cli;
        this.application = application;
        this.build = build;
        this.environment = environment;
        this.dependencies = dependencies;
        this.modules = modules;
        this.fromCache = false;
        this.classpaths = new HashMap<>();
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
        this.classpaths = new HashMap<>();
        this.configuration = context.getConfiguration();
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
    public List<Module> getModules() {
        return modules;
    }
}
