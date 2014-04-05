package org.m410.fab.config;

import org.m410.fab.config.Application;
import org.m410.fab.config.Build;
import org.m410.fab.config.Dependency;
import org.m410.fab.config.Module;

import java.util.List;

/**
 * @author m410
 */
public class ConfigContext {
    private Application application;
    private Build build;
    private List<Dependency> dependencies;
    private List<Module> modules;


    public ConfigContext(Application application, Build build, List<Dependency> dependencies, List<Module> modules) {
        this.application = application;
        this.build = build;
        this.dependencies = dependencies;
        this.modules = modules;
    }

    public Application getApplication() {
        return application;
    }

    public Build getBuild() {
        return build;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public List<Module> getModules() {
        return modules;
    }
}
