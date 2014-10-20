package org.m410.fabricate.config;

import java.util.*;
import java.util.stream.Collectors;





/**
 * @author m410
 */
public final class ConfigContext {
    private Application application;
    private Build build;
    private List<Dependency> dependencies;
    private List<Module> modules;

    @SuppressWarnings("unchecked")
    public ConfigContext(Map<String,Object> base) {
        application = new ApplicationImpl((Map<String,Object>)base.get("application"));
        build = new BuildImpl((Map<String,Object>)base.get("build"));
        dependencies = ((List<Map<String,Object>>)base
                .getOrDefault("dependencies", new ArrayList<>()))
                .stream().map(Dependency::new)
                .collect(Collectors.toList());

        modules = new ArrayList<>();

        Optional.ofNullable(base.getOrDefault("modules", new ArrayList<>())).ifPresent(m ->{
            modules.addAll(((List<Map<String,Object>>)m).stream().map(ModuleImpl::new).collect(Collectors.toList()));
        });

        Optional.ofNullable(base.getOrDefault("logging", new ArrayList<>())).ifPresent(m ->{
            if(m instanceof List)
                modules.addAll(((List<Map<String,Object>>)m).stream().map(ModuleImpl::new).collect(Collectors.toList()));
        });

        Optional.ofNullable(base.getOrDefault("test", new ArrayList<>())).ifPresent(m ->{
            modules.addAll(((List<Map<String,Object>>)m).stream().map(ModuleImpl::new).collect(Collectors.toList()));
        });

        Optional.ofNullable(base.getOrDefault("persistence", new ArrayList<>())).ifPresent(m -> {
            modules.addAll(((List<Map<String, Object>>) m).stream().map(ModuleImpl::new).collect(Collectors.toList()));
        });

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
