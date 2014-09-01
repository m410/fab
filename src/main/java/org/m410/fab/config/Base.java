package org.m410.fab.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author m410
 */
public abstract class Base {
    protected URL url;
    protected Map<String,Object> configuration;

    List<BundleRef> getBundles() throws MalformedURLException {
        return null;
    }

    public URL makeUrl(Map<String,String> resource) throws MalformedURLException {
        if(resource.get("base_config") != null)
            return new URL(resource.get("base_config"));
        else
            return new URL(
                    "http://repo.m410.org/content/repositories/snapshots/" +
                            resource.get("organization").replaceAll("\\.", "/") +
                            "/" +
                            resource.get("name") +
                            "/" +
                            resource.get("version") + ".yml");
    }
}
