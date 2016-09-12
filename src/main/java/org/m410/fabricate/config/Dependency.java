package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

/**
 * @author m410
 */
public final class Dependency implements Comparable<Dependency>{
    private final String org;
    private final String name;
    private final String rev;
    private final String scope;
    private final boolean transitive;

    public Dependency(String scope, String org, String name, String rev, boolean transitive) {
        this.scope = scope;
        this.org = org;
        this.name = name;
        this.rev = rev;
        this.transitive = transitive;
    }

    public Dependency(ImmutableHierarchicalConfiguration config) {
        org = config.getString("org");
        name = config.getString("name");
        rev = config.getString("rev");
        scope = config.getString("scope");
        transitive = config.getBoolean("transitive", Boolean.FALSE);
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
    public int compareTo(Dependency o) {
        return this.org.compareTo(o.org) +
               this.name.compareTo(o.name) +
               this.rev.compareTo(o.rev); // todo need better revision comparison
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Dependency that = (Dependency) o;

        if (!org.equals(that.org)) {
            return false;
        }
        if (!name.equals(that.name)) {
            return false;
        }
        return rev.equals(that.rev);

    }

    @Override
    public int hashCode() {
        int result = org.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + rev.hashCode();
        return result;
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
