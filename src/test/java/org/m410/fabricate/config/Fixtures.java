package org.m410.fabricate.config;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.UnionCombiner;
import org.m410.config.YamlConfig;
import org.m410.config.YamlConfiguration;

/**
 * @author Michael Fortin
 */
public final class Fixtures {

    public static ImmutableHierarchicalConfiguration config() throws ConfigurationException {
        CombinedConfiguration combined = new CombinedConfiguration(new UnionCombiner());

        final YamlConfiguration envConfig = YamlConfig.load("src/test/resources/garden.test.fab.yml", "development");
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


        return new YamlConfiguration(combined);
    }
}
