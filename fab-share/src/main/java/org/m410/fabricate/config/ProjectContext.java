package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    private final List<BuildModule> buildModules;
    private final ImmutableHierarchicalConfiguration configuration;

    public ProjectContext(ImmutableHierarchicalConfiguration configuration) {
        this.configuration = configuration;
        this.application = new ApplicationImpl(configuration.immutableConfigurationAt("application"));
        this.build = new BuildImpl(configuration.immutableSubset("build"));
        this.dependencies = extractDependencies(configuration);
        this.buildModules = extractModules(configuration);
    }

    private List<Dependency> extractDependencies(ImmutableHierarchicalConfiguration conf) {
        if (conf.containsKey("dependencies")) {
            return (List<Dependency>) IntStream.range(0, conf.getMaxIndex("dependencies") + 1)
                    .mapToObj(i -> new Dependency(conf.immutableConfigurationAt("dependencies(" + i + ")")))
                    .collect(Collectors.toList());
        }
        else {
            return new ArrayList<>();
        }
    }

    private List<BuildModule> extractModules(ImmutableHierarchicalConfiguration configuration) {
        final Iterator<String> keys = configuration.getKeys();
        List<String> modules = new ArrayList<>();

        while (keys.hasNext()) {
            final String next = keys.next();

            if(modulePattern.matcher(next).matches()) {
                modules.add(next);
            }
        }

        return modules.stream()
                .map(s -> new BuildModuleImpl(s, configuration.immutableConfigurationAt(s)))
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

    public List<BuildModule> getModules() {
        return buildModules;
    }

    public ImmutableHierarchicalConfiguration getConfiguration() {
        return configuration;
    }
}
