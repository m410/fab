package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.util.Map;

/**
 * @author m410
 */
public class ApplicationImpl implements Application {
    private String name;
    private String org;
    private String version;
    private ImmutableHierarchicalConfiguration properties;
    private String applicationClass;
    private String authors;
    private String description;

    public ApplicationImpl() {
    }

    public ApplicationImpl(ImmutableHierarchicalConfiguration config) {
        name = config.getString("name");
        org = config.getString("org");
        description = config.getString("description");
        version = config.getString("version");
        applicationClass = config.getString("applicationClass");
        authors = config.getString("authors");

        properties = config.containsKey("properties") ?
                     config.immutableConfigurationAt("properties") :
                    null;  // todo shouldn't be null, either optional or default empty
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
    public ImmutableHierarchicalConfiguration getProperties() {
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
