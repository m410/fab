package org.m410.fab.builder;

import org.m410.fab.config.Application;
import org.m410.fab.config.Build;
import org.m410.fab.config.Dependency;
import org.m410.fab.config.Module;

import java.util.List;

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
    public Application application() {
        return application;
    }

    @Override
    public Build build() {
        return build;
    }

    @Override
    public Cli cli() {
        return cli;
    }

    @Override
    public String environment() {
        return environment;
    }

    @Override
    public List<Dependency> dependencies() {
        return dependencies;
    }

    @Override
    public List<? extends Module> modules() {
        return modules;
    }
}
