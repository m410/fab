package org.m410.fab.builder;

import org.m410.fab.config.*;
import org.m410.fab.service.internal.serialize.CachedProject;

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
    }

    public BuildContextImpl(Cli cli, ConfigContext context, String hash, String environment) {
        this.hash = hash;
        this.fromCache = false;
        this.cli = cli;
        this.application = context.getApplication();
        this.build = context.getBuild();
        this.environment = environment;
        this.dependencies = context.getDependencies();
        this.modules = context.getModules();
        this.classpaths = new HashMap<>();
    }

    public BuildContextImpl(CliStdOutImpl cli, CachedProject cacheProj, String env) {
        this.cli = cli;
        this.hash = cacheProj.getHash();
        this.application = cacheProj.getApplication();
        this.build = cacheProj.getBuild();
        this.environment = env;
        this.dependencies = cacheProj.getDependencies();
        this.modules = cacheProj.getModules();
        this.classpaths = cacheProj.getClasspath();
        this.fromCache = true;
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
