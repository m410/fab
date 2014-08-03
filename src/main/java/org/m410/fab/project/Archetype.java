package org.m410.fab.project;

import java.net.URL;

/**
 * @author m410
 */
public class Archetype {
    private String name;
    private String version;
    private String organization;
    private URL base;

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

    public URL getBase() {
        return base;
    }

    public void setBase(URL base) {
        this.base = base;
    }

    @Override
    public String toString() {
        return "Archetype{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", organization='" + organization + '\'' +
                ", url=" + base +
                '}';
    }
}
