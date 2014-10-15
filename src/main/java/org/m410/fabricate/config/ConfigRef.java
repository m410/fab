package org.m410.fabricate.config;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @author m410
 */
public final class ConfigRef extends Base{

    @SuppressWarnings("unchecked")
    public ConfigRef(URL url) throws IOException {
        this.url = url;
        this.configuration = (Map<String,Object>)new Yaml().load(url.openStream());
    }
}
