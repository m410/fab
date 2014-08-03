package org.m410.fab.project;

import java.net.URL;

/**
 * @author m410
 */
public class Archetype {
    private String name;
    private String version;
    private String organization;
    private URL base_config;
    private URL base_build;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public URL getBase_config() {
        return base_config;
    }

    public void setBase_config(URL base_config) {
        this.base_config = base_config;
    }

    public URL getBase_build() {
        return base_build;
    }

    public void setBase_build(URL base_build) {
        this.base_build = base_build;
    }

    @Override
    public String toString() {
        return "Archetype{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", organization='" + organization + '\'' +
                ", url=" + base_config +
                '}';
    }
}
