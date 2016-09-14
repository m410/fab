package org.m410.fabricate.config;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;

import java.net.URL;
import java.util.Optional;

/**
 * @author Michael Fortin
 */
public abstract class ReferenceBase implements Reference {
    protected String name;
    protected String org;
    protected String version;
    protected Type type;
    protected Level level;
    protected String environment;
    protected BaseHierarchicalConfiguration configuration;
    protected URL remoteReference;
    protected String packageType = "jar";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOrg() {
        return org;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public String getEnv() {
        return environment;
    }

    @Override
    public BaseHierarchicalConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public Optional<URL> getRemoteReference() {
        return Optional.ofNullable(remoteReference);
    }

    @Override
    public String toMavenPath() {
        return "/" + org.replaceAll("\\.", "/") + "/" + name + "/" + version + "/" + name + "-" + version + "." +
               packageType;
    }

    @Override
    public String toMavenSnapshotMetadata() {
        return "/" + org.replaceAll("\\.", "/") + "/" + name + "/" + version + "/maven-metadata.xml";
    }

    @Override
    public boolean isMavenSnapshot() {
        return version.endsWith("SNAPSHOT");
    }

    @Override
    public String toSnapshotPath(String sVersion) {
        final String orgOut = org.replaceAll("\\.", "/");
        return "/" + orgOut + "/" + name + "/" + version + "/" + name + "-" + sVersion + "." + packageType;
    }

    @Override
    public String toString() {
        return "Reference{" +
               "name='" + name + '\'' +
               ", org='" + org + '\'' +
               ", version='" + version + '\'' +
               ", type=" + type +
               ", level=" + level +
               ", environment='" + environment + '\'' +
               ", remoteReference=" + remoteReference +
               '}';
    }
}
