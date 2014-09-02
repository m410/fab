package org.m410.fab.builder;

import org.m410.fab.config.Application;
import org.m410.fab.config.Build;
import org.m410.fab.config.Dependency;
import org.m410.fab.config.Module;

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
    private final Map<String,String> classpaths = new HashMap<>();
    private String hash;

    public BuildContextImpl(Cli cli, Application application, Build build, String environment,
                            List<Dependency> dependencies, List<Module> modules) {
        this.cli = cli;
        this.application = application;
        this.build = build;
        this.environment = environment;
        this.dependencies = dependencies;
        this.modules = modules;
    }

    @Override
    public String getHash() {
        return hash;
    }

    @Override
    public void setHash(String hash) {
        this.hash = hash;
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
    public List<? extends Module> getModules() {
        return modules;
    }
}
