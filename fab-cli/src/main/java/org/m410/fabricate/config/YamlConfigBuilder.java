package org.m410.fabricate.config;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.tree.UnionCombiner;
import org.m410.config.YamlConfiguration;

import java.io.StringWriter;
import java.util.List;

/**
 * @author Michael Fortin
 */
public final class YamlConfigBuilder {
    private String env;
    private List<Reference> localEnvReferences;
    private List<Reference> projectEnvReferences;
    private BaseHierarchicalConfiguration project;
    private List<Reference> moduleBaseReferences;
    private Reference archetypeReference;


    public YamlConfigBuilder(String env, List<Reference> localEnvReferences, List<Reference> projectEnvReferences,
            BaseHierarchicalConfiguration project, List<Reference> moduleBaseReferences, Reference archetypeReference) {
        this.env = env;
        this.localEnvReferences = localEnvReferences;
        this.projectEnvReferences = projectEnvReferences;
        this.project = project;
        this.moduleBaseReferences = moduleBaseReferences;
        this.archetypeReference = archetypeReference;
    }

    public static YamlConfigBuilder builder() {
        return new YamlConfigBuilder(null, null, null, null, null, null);
    }


    public YamlConfigBuilder withEnv(String env) {
        return new YamlConfigBuilder(
                env,
                localEnvReferences,
                projectEnvReferences,
                project,
                moduleBaseReferences,
                archetypeReference
        );
    }

    public YamlConfigBuilder addLocalEnvs(List<Reference> localEnvReferences) {
        return new YamlConfigBuilder(
                env,
                localEnvReferences,
                projectEnvReferences,
                project,
                moduleBaseReferences,
                archetypeReference
        );
    }

    public YamlConfigBuilder addProjectEnvs(List<Reference> projectEnvReferences) {
        return new YamlConfigBuilder(
                env,
                localEnvReferences,
                projectEnvReferences,
                project,
                moduleBaseReferences,
                archetypeReference
        );
    }

    public YamlConfigBuilder addProject(BaseHierarchicalConfiguration project) {
        return new YamlConfigBuilder(
                env,
                localEnvReferences,
                projectEnvReferences,
                project,
                moduleBaseReferences,
                archetypeReference
        );
    }


    public YamlConfigBuilder addModules(List<Reference> moduleBaseReferences) {
        return new YamlConfigBuilder(
                env,
                localEnvReferences,
                projectEnvReferences,
                project,
                moduleBaseReferences,
                archetypeReference
        );
    }

    public YamlConfigBuilder addArchetype(Reference archetypeReference) {
        return new YamlConfigBuilder(
                env,
                localEnvReferences,
                projectEnvReferences,
                project,
                moduleBaseReferences,
                archetypeReference
        );
    }

    public BaseHierarchicalConfiguration make() {
        CombinedConfiguration combined = new CombinedConfiguration(new UnionCombiner());

        localEnvReferences.stream()
                .filter(r -> r.getEnv().equals(env))
                .findFirst()
                .ifPresent(r -> combined.addConfiguration(r.getConfiguration(), "local-env"));

        projectEnvReferences.stream()
                .filter(r -> !r.getEnv().equals("default"))
                .filter(r -> r.getEnv().equals(env))
                .findFirst()
                .ifPresent(r -> combined.addConfiguration(r.getConfiguration(), "project-env"));

        combined.addConfiguration(project, "project-default");

        moduleBaseReferences.stream()
                .filter(r -> r.getLevel() == Reference.Level.REMOTE)
                .forEach(r -> {
                    try (StringWriter writer = new StringWriter()) {
                        ((YamlConfiguration) r.getConfiguration()).write(writer);
                        combined.addConfiguration(r.getConfiguration(), "remote-" + r.getName());
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        combined.addConfiguration(archetypeReference.getConfiguration(), "remote-archetype");

        return new YamlConfiguration(combined);
    }
}
