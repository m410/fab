package org.m410.fab.config;

import java.util.Map;

/**
 * @author m410
 */
public class ModuleImpl implements Module {
    private Type type;
    private String name;
    private String org;
    private String version;
    private Map<String,Object> properties;

    public ModuleImpl(Map<String,Object> data) {
//        type = (String)data.get("type");
        name = (String)data.get("name");
        org = (String)data.get("org");
        version = (String)data.get("version");
        properties = data;
        properties.remove("name");
        properties.remove("org");
        properties.remove("version");
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOrg() {
        return org;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }
}
