package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.util.*;
import java.util.stream.Collectors;





/**
 * @author m410
 */
public final class ConfigContext {
    private final Application application;
    private final Build build;
    private final List<Dependency> dependencies;
    private final List<Module> modules;
    private final ImmutableHierarchicalConfiguration configuration;

    public ConfigContext(ImmutableHierarchicalConfiguration configuration) {
        this.configuration = configuration;
        // todo create others
        this.application = null;
        this.build = null;
        this.dependencies = null;
        this.modules = null;

        // todo replace with new module syntax 'module(group:name:version)'
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public ConfigContext(Map<String,Object> base) {
        this.configuration = null;
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

        Optional.ofNullable(base.getOrDefault("logging", new ArrayList<>())).ifPresent(m -> {
            modules.addAll(((List<Map<String, Object>>) m).stream().map(ModuleImpl::new).collect(Collectors.toList()));
        });

        Optional.ofNullable(base.getOrDefault("testing", new ArrayList<>())).ifPresent(m ->{
            modules.addAll(((List<Map<String,Object>>)m).stream().map(ModuleImpl::new).collect(Collectors.toList()));
        });

        Optional.ofNullable(base.getOrDefault("persistence", new ArrayList<>())).ifPresent(m -> {
            modules.addAll(((List<Map<String, Object>>) m).stream().map(ModuleImpl::new).collect(Collectors.toList()));
        });

        Optional.ofNullable(base.getOrDefault("views", new ArrayList<>())).ifPresent(m -> {
            modules.addAll(((List<Map<String, Object>>) m).stream().map(ModuleImpl::new).collect(Collectors.toList()));
        });
    }

    public ConfigContext(Application application, Build build, List<Dependency> dependencies, List<Module> modules) {
        this.configuration = null;
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

    public ImmutableHierarchicalConfiguration getConfiguration() {
        return configuration;
    }
}
