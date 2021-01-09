package org.m410.fabricate.config;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.m410.config.YamlConfig;

import java.io.File;

/**
 * @author Michael Fortin
 */
public final class RemoteReference extends ReferenceBase {
    private Reference projectReference;

    public RemoteReference(Reference localRef, File localCacheFile) {
        this.name = localRef.getName();
        this.org = localRef.getOrg();
        this.version = localRef.getVersion();
        this.type = localRef.getType();
        this.level = Level.REMOTE;
        this.environment = "default";
        this.projectReference = localRef;

        if (org == null || name == null || version == null) {
            throw new InvalidConfigurationException("invalid remote declaration: '" + org + "','" + name + "','" +
                                                    version + "'");
        }

        try {
            this.configuration = YamlConfig.load(localCacheFile);
        }
        catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
