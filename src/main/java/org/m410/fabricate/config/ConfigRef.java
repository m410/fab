package org.m410.fabricate.config;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @author m410
 */
public final class ConfigRef extends Base {

    @SuppressWarnings("unchecked")
    public ConfigRef(URL url) {
        this.url = url;
        try {
            this.configuration = (Map<String,Object>)new Yaml().load(url.openStream());
        }
        catch (IOException e) {
            throw new InvalidConfigurationException(url.toString(),e);
        }
    }
}
