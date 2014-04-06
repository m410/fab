package org.m410.fab.config;

import java.util.List;
import java.util.Map;

/**
 * @author m410
 */
public final class ConfigBuilder  {
    private Map<String,Object> projectConfiguration;
    private List<Map<String,Object>> envConfig;

    public ConfigBuilder(Map<String, Object> projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }

    @SuppressWarnings("unchecked")
    public ConfigBuilder parseLocalProject() {
        envConfig = (List<Map<String,Object>> )projectConfiguration.get("environment_overrides");
        projectConfiguration.remove("environment_overrides");
        return this;
    }

    public ConfigBuilder applyUnder(List<Map<String,Object>> parentConfigurations) {
        for (Map<String, Object> parentConfiguration : parentConfigurations)
            applyUnderProject(parentConfiguration);

        return this;
    }

    public ConfigBuilder applyEnvOver(String env) {

        for (Map<String, Object> override : envConfig)
            if(override.get("environment").equals(env))
                applyOverProject(override);

        return this;
    }

    void applyOverProject(Map<String,Object> over) {

    }

    void applyUnderProject(Map<String,Object> under) {

    }

    public ConfigContext build() {
        return new ConfigContext(projectConfiguration);
    }
}
