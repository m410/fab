package org.m410.fabricate.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @author m410
 */
public final class BundleRef {
    private String name;
    private String org;
    private String version;
    private URL url;
    private String symbolicName;

    public BundleRef(Map<String,String> map) throws MalformedURLException {
        this.name = map.get("name");
        this.org = map.get("org");
        this.version= map.get("version");
        this.url = map.containsKey("url") ? new URL(map.get("url")) : null;
        this.symbolicName = map.get("symbolicName");
    }

    public URL makeUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getSymbolicName() {
        if(symbolicName != null)
            return symbolicName;
        else
            return name;
    }

    public void setSymbolicName(String symbolicName) {
        this.symbolicName = symbolicName;
    }

    @Override
    public String toString() {
        return "BundleRef(" +
                "name='" + name + '\'' +
                ", org='" + org + '\'' +
                ", version='" + version + '\'' +
                ", url=" + url +
                ')';
    }

}
