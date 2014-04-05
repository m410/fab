package org.m410.fab.config;

import java.util.List;
import java.util.Map;

/**
 * @author m410
 */
public final class ConfigBuilder  {
    private Map<String,Object> projectConfiguration;

    public ConfigBuilder(Map<String, Object> projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }

    public ConfigBuilder parseLocalProject() {


        return this;
    }

    public ConfigBuilder applyUnder(List<Map<String,Object>> parentConfigurations) {

        return this;
    }

    public ConfigBuilder applyEnvOver(String env) {

        return this;
    }

    public ConfigContext build() {
        return null;
    }
}
