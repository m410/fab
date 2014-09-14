package org.m410.fab.config;

import java.util.HashMap;
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

//    public ConfigBuilder applyUnder(List<Map<String,Object>> parentConfigurations) {
//        parentConfigurations.stream().forEach(p -> merge(p,projectConfiguration));
//        return this;
//    }

    public ConfigBuilder applyOver(Map<String,Object> base) {
        HashMap<String, Object> outMap = new HashMap<>();
        merge(outMap,base);
        merge(outMap,projectConfiguration);
        this.projectConfiguration = outMap;
        return this;
    }

    public ConfigBuilder applyEnvOver(String env) {
        if(envConfig != null && envConfig.size() > 0)
            envConfig.stream()
                    .filter(e -> e.get("environment").equals(env))
                    .forEach(e -> merge(projectConfiguration, e));
        return this;
    }

    @SuppressWarnings("unchecked")
    void merge(Map<String, Object> under, Map<String, Object> over) {
        for (String overKey : over.keySet()) {
            Object underValue = under.get(overKey);

            if(underValue != null) {

                if(underValue instanceof List){
                    List baseListValues = (List)underValue;
                    List overListValues = (List)over.get(overKey);

                    if(overListValues != null)
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
                            else
                                baseListValues.add(overMap);
                        }
                }
                else if(underValue instanceof Map) {
                    Map baseMapValues = (Map)underValue;
                    Map overMapValues = (Map)over.get(overKey);
                    merge(baseMapValues, overMapValues);
                }
                else {
                    under.put(overKey, over.get(overKey));
                }
            }
            else {
                under.put(overKey, over.get(overKey));
            }
        }
    }

    public ConfigContext build() {
        return new ConfigContext(projectConfiguration);
    }
}
