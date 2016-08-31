package org.m410.fabricate.config;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * @author m410
 */
public final class Archetype extends ReferenceBase {
    private final URL base_build;


    public Archetype(ImmutableHierarchicalConfiguration configuration) {
        this.name = configuration.getString("name");
        this.org = configuration.getString("organization");
        this.version = configuration.getString("version");
        this.configuration = (BaseHierarchicalConfiguration)configuration;

        try {
            this.base_build = configuration.containsKey("base_build") ?
                    new URL(configuration.getString("base_build")) :
                    null;
            this.remoteReference = configuration.containsKey("remote_reference") ?
                    new URL(configuration.getString("remote_reference")) :
                    null;
        }
        catch (MalformedURLException e) {
            throw new InvalidConfigurationException("invalid url",e);
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
