package org.m410.fabricate.config;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author m410
 */
public final class Archetype extends ReferenceBase {
    private final URL base_build;


    public Archetype(BaseHierarchicalConfiguration configuration) {
        this.name = configuration.getString("archetype.name");
        this.org = configuration.getString("archetype.organization");
        this.version = configuration.getString("archetype.version");
        this.configuration = configuration;
        this.packageType = "yml";

        try {
            this.base_build = configuration.containsKey("archetype.base_build") ?
                      new URL(configuration.getString("archetype.base_build")) :
                      null;
            this.remoteReference = configuration.containsKey("archetype.remote_reference") ?
                      new URL(configuration.getString("archetype.remote_reference")) :
                      null;
        }
        catch (MalformedURLException e) {
            throw new InvalidConfigurationException("invalid url", e);
        }

        if (org == null || name == null || version == null) {
            throw new InvalidConfigurationException(
                    "invalid module declaration: '" + org + "','" + name + "','" + version + "'");
        }
    }

    public URL getBase_build() {
        return base_build;
    }

    @Override
    public String toString() {
        return "Archetype{" +
               "name='" + name + '\'' +
               ", version='" + version + '\'' +
               ", organization='" + org + '\'' +
               ", url=" + remoteReference +
               '}';
    }
}
