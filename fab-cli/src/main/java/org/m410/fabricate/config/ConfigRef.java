package org.m410.fabricate.config;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A reference to a configuration for a module, or archetype.
 *
 * @author m410
 */
public final class ConfigRef extends ReferenceBase {

    public ConfigRef(BaseHierarchicalConfiguration c, Type type, Level level, String env) {
        this.environment = env;
        this.type = type;
        this.level = level;
        this.configuration = c;
        this.name = c.getString("name");
        this.org = c.getString("org");
        this.version = c.getString("version");

        try {
            this.remoteReference = c.containsKey("remote_reference") ?
                                   new URL(c.getString("remote_reference")) :
                                   null;
        }
        catch (MalformedURLException e) {
            throw new InvalidConfigurationException("invalid url: " + c.getString("remote_reference"), e);
        }
    }
}
