package org.m410.fabricate.config;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.UnionCombiner;
import org.m410.config.YamlConfiguration;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Michael Fortin
 */
public final class ConfigContextBuilder {
    private String env;
    private Collection<ConfigProvider> configProviders;

    ConfigContextBuilder() {
    }

    public static ConfigContextBuilder builder() {
        return new ConfigContextBuilder();
    }

    public ConfigContext make() throws ConfigurationException {
        CombinedConfiguration combined = new CombinedConfiguration(new UnionCombiner());
        findIn(env,ConfigProvider.Type.LOCAL)
                .ifPresent(p -> combined.addConfiguration(p.configuration(),"LOCAL"));
        findIn(env,ConfigProvider.Type.PROJECT)
                .ifPresent(p -> combined.addConfiguration(p.configuration(),"ENV"));
        findIn("default",ConfigProvider.Type.PROJECT)
                .ifPresent(p -> combined.addConfiguration(p.configuration(),"PROJECT"));
        listIn("default",ConfigProvider.Type.MODULE)
                .forEach(p -> combined.addConfiguration(p.configuration(),"MODULE"));
        findIn("default",ConfigProvider.Type.ARCHETYPE)
                .ifPresent(p -> combined.addConfiguration(p.configuration(),"ARCHETYPE"));
        ImmutableHierarchicalConfiguration configuration = new YamlConfiguration(combined);

        return new ConfigContext(configuration);
    }

    private List<ConfigProvider> listIn(String aDefault, ConfigProvider.Type module) {
        return configProviders.stream()
                .filter(p -> p.getType() == module)
                .filter(p -> p.getEnvName().equalsIgnoreCase(aDefault))
                .collect(Collectors.toList());
    }

    public ConfigContextBuilder env(String env) {
        this.env = env;
        return this;
    }

    public ConfigContextBuilder providers(List<ConfigProvider> configProviders) {
        this.configProviders = configProviders;
        return this;
    }

    private Optional<ConfigProvider> findIn(String env, ConfigProvider.Type type) {
        return configProviders.stream().filter(p -> p.getType() == type).filter(p -> p
                .getEnvName().equalsIgnoreCase(env)).findFirst();
    }
}
