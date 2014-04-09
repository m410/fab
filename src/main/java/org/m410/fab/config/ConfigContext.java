package org.m410.fab.config;

import org.m410.fab.config.Application;
import org.m410.fab.config.Build;
import org.m410.fab.config.Dependency;
import org.m410.fab.config.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author m410
 */
public class ConfigContext {
    private Application application;
    private Build build;
    private List<Dependency> dependencies;
    private List<Module> modules;

    @SuppressWarnings("unchecked")
    public ConfigContext(Map<String,Object> base) {
        application = new ApplicationImpl((Map<String,Object>)base.get("application"));
        build = new BuildImpl((Map<String,Object>)base.get("build"));
        dependencies = ((List<Map<String,Object>>)base.getOrDefault("dependencies", new HashMap<>()))
                .stream().map(Dependency::new).collect(Collectors.toList());
        modules = ((List<Map<String,Object>>)base.getOrDefault("modules", new ArrayList<>()))
                .stream().map(ModuleImpl::new).collect(Collectors.toList());
        modules.addAll(((List<Map<String, Object>>) base.getOrDefault("logging", new ArrayList<>()))
                .stream().map(ModuleImpl::new).collect(Collectors.toList()));
        modules.addAll(((List<Map<String,Object>>)base.getOrDefault("persistence", new ArrayList<>()))
                .stream().map(ModuleImpl::new).collect(Collectors.toList()));
        modules.addAll(((List<Map<String, Object>>) base.getOrDefault("view", new ArrayList<>()))
                .stream().map(ModuleImpl::new).collect(Collectors.toList()));
    }

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
