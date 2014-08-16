package org.m410.fab.config;

import java.util.Map;

/**
 * @author m410
 */
public class Dependency {
    private String org;
    private String name;
    private String rev;
    private String scope;
    private boolean transitive = false;

    public Dependency(Map<String,Object> data) {
        org = (String)data.get("org");
        name = (String)data.get("name");
        rev = (String)data.get("rev");
        scope = (String)data.get("scope");
        transitive = (boolean)data.getOrDefault("transitive",false);
    }

    public Dependency(String scope, String org, String name, String rev, boolean transitive) {
        this.scope = scope;
        this.org = org;
        this.name = name;
        this.rev = rev;
        this.transitive = transitive;
    }

    public String getOrg() {
        return org;
    }

    public String getName() {
        return name;
    }

    public String getRev() {
        return rev;
    }

    public String getScope() {
        return scope;
    }

    public boolean isTransitive() {
        return transitive;
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "org='" + org + '\'' +
                ", name='" + name + '\'' +
                ", rev='" + rev + '\'' +
                ", scope='" + scope + '\'' +
                ", transitive=" + transitive +
                '}';
    }
}
