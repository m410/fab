package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author m410
 */
public final class ProjectContext {
    private final Pattern modulePattern = Pattern.compile("^(persistence|modules|views|testing|logging)\\(.*?\\)$");

    private final Application application;
    private final Build build;
    private final List<Dependency> dependencies;
    private final List<Module> modules;
    private final ImmutableHierarchicalConfiguration configuration;

    public ProjectContext(ImmutableHierarchicalConfiguration configuration) {
        this.configuration = configuration;
        this.application = new ApplicationImpl(configuration.immutableConfigurationAt("application"));
        this.build = new BuildImpl(configuration.immutableSubset("build"));
        this.dependencies = extractDependencies(configuration);
        this.modules = extractModules(configuration);
    }

    private List<Dependency> extractDependencies(ImmutableHierarchicalConfiguration configuration) {
        System.out.println("dependency count: " + configuration.getMaxIndex("dependencies"));
        System.out.println(configuration.get(Map.class, "dependencies(0)"));
        final ImmutableHierarchicalConfiguration x = configuration.immutableConfigurationAt("dependencies(0)");
        System.out.println(x);
        System.out.println(x.getString("name"));

        return (List<Dependency>)IntStream.range(0, configuration.getMaxIndex("dependencies") + 1)
                .mapToObj(i -> new Dependency(configuration.get(Map.class, "dependencies(" + i + ")")))
                .collect(Collectors.toList());
    }

    private List<Module> extractModules(ImmutableHierarchicalConfiguration configuration) {
        final Iterator<String> keys = configuration.getKeys();
        List<String> modules = new ArrayList<>();

        while (keys.hasNext()) {
            final String next = keys.next();

            if(modulePattern.matcher(next).matches()) {
                modules.add(next);
            }
        }

        return modules.stream()
                .map(s -> new ModuleImpl(s, configuration.immutableConfigurationAt(s)))
                .collect(Collectors.toList());
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
