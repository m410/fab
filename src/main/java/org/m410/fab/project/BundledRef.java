package org.m410.fab.project;

import java.net.URL;

/**
 * @author m410
 */
@Deprecated
public class BundledRef {
    private String name;
    private String org;
    private String version;
    private URL url;
    private String symbolicName;

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
