package org.m410.fabricate.config;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.UnionCombiner;
import org.m410.config.YamlConfiguration;
import org.m410.config.YamlEnvConfiguration;

/**
 * @author Michael Fortin
 */
public final class Fixtures {

    public static ImmutableHierarchicalConfiguration config() throws ConfigurationException {
        System.setProperty("fabricate.env","development");
        CombinedConfiguration combined = new CombinedConfiguration(new UnionCombiner());

        final YamlConfiguration envConfig = new FileBasedConfigurationBuilder<>(YamlEnvConfiguration.class)
                .configure(new Parameters().hierarchical().setFileName("src/test/resources/garden.test.fab.yml"))
                .getConfiguration();
        combined.addConfiguration(envConfig,"env");

        final YamlConfiguration overlapConfig = new FileBasedConfigurationBuilder<>(YamlConfiguration.class)
                .configure(new Parameters().hierarchical().setFileName("src/test/resources/garden.test.fab.yml"))
                .getConfiguration();
        combined.addConfiguration(overlapConfig,"app");

        final YamlConfiguration moduleConfig = new FileBasedConfigurationBuilder<>(YamlConfiguration.class)
                .configure(new Parameters().hierarchical().setFileName("src/test/resources/garden.persistence.fab.yml"))
                .getConfiguration();
        combined.addConfiguration(moduleConfig,"module");

        final YamlConfiguration defaultConfig = new FileBasedConfigurationBuilder<>(YamlConfiguration.class)
                .configure(new Parameters().hierarchical().setFileName("src/test/resources/garden.default.fab.yml"))
                .getConfiguration();
        combined.addConfiguration(defaultConfig,"default");

        System.clearProperty("fabricate.env");

        return new YamlConfiguration(combined);
    }
}
