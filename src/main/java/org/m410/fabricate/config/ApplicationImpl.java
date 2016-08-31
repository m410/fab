package org.m410.fabricate.config;

import java.util.Map;

/**
 * @author m410
 */
public class ApplicationImpl implements Application {
    private String name;
    private String org;
    private String version;
    private Map<String,Object> properties;
    private String applicationClass;
    private String authors;
    private String description;

    public ApplicationImpl() {
    }

    // todo ApplicationImpl(ImmutableHierarchicalConfiguration config) {}

    @SuppressWarnings("unchecked")
    @Deprecated
    public ApplicationImpl(Map<String, Object> data) {
        name = (String)data.get("name");
        org = (String)data.get("org");
        description = (String)data.get("description");
        version = (String)data.get("version");
        applicationClass = (String)data.get("applicationClass");
        authors = (String)data.get("authors");
        properties = (Map<String,Object>)data.get("properties");
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

    @Override
    public String getApplicationClass() {
        return applicationClass;
    }

    @Override
    public String getAuthors() {
        return authors;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setApplicationClass(String applicationClass) {
        this.applicationClass = applicationClass;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ApplicationImpl{" +
                "name='" + name + '\'' +
                ", org='" + org + '\'' +
                ", version='" + version + '\'' +
                ", properties=" + properties +
                ", applicationClass='" + applicationClass + '\'' +
                ", authors='" + authors + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
