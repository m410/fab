package org.m410.fab.service;

import org.m410.fab.config.ConfigProvider;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author m410
 */
public class ConfigProviderImpl implements ConfigProvider {
    private Map<String, Object> fullConfig;

    public ConfigProviderImpl(Map<String, Object> fullConfig) {
        this.fullConfig = fullConfig;
    }

    @Override
    public Map<String, Object> config() {
        return fullConfig;
    }

    @Override
    public Set<String> validate(Map<String, Object> fullConfig) {
        return new HashSet<>(0);
    }

    @Override
    public String toString() {
        return "ConfigProviderImpl{" +
                "fullConfig=" + fullConfig +
                '}';
    }
}
