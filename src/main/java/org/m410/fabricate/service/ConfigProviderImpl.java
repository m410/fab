package org.m410.fabricate.service;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.m410.fabricate.config.ConfigProvider;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author m410
 */
@Deprecated
public final class ConfigProviderImpl implements ConfigProvider {
    private Configuration config;
    private String envName;
    private Type type;

    public ConfigProviderImpl(Configuration config, String envName, Type type) {
        this.config = config;
        this.envName = envName;
        this.type = type;
    }

    @Override
    public Set<String> validate(ImmutableHierarchicalConfiguration fullConfig) {
        return new HashSet<>();
    }

    public Configuration configuration() {
        return config;
    }

    @Override
    public String getEnvName() {
        return envName;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ConfigProviderImpl{" +
                "config=" + config +
                '}';
    }
}
