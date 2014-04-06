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
            merge(parentConfiguration, projectConfiguration);

        return this;
    }

    public ConfigBuilder applyEnvOver(String env) {

        for (Map<String, Object> override : envConfig)
            if(override.get("environment").equals(env))
                merge(projectConfiguration, override);

        return this;
    }

    void merge(Map<String, Object> under, Map<String, Object> over) {
        for (String s : over.keySet()) {
            Object baseValue = under.get(s);

            if(baseValue != null) {

                if(baseValue instanceof List){
                    List baseListValues = (List)baseValue;
                    List overListValues = (List)over.get(s);

                    for (Object overObj : overListValues) {
                        Map<String,Object> overMap = (Map<String,Object>)overObj;
                        String mapName = (String)overMap.get("name");
                        Map<String,Object> baseMap = null;

                        for (Object row : baseListValues) {
                            Map<String,Object> map = (Map<String,Object>)row;

                            if(mapName.equals(map.get("name")))
                                baseMap = map;
                        }

                        if(baseMap != null)
                            merge(baseMap, overMap);
                    }
                }
                else if(baseValue instanceof Map) {
                    Map baseMapValues = (Map)baseValue;
                    Map overMapValues = (Map)over.get(s);
                    merge(baseMapValues, overMapValues);
                }
                else {
                    under.put(s, over.get(s));
                }
            }
            else {
                under.put(s, over.get(s));
            }
        }
    }

    public ConfigContext build() {
        return new ConfigContext(projectConfiguration);
    }
}
