package org.m410.fabricate.config;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;

import java.net.URL;
import java.util.Optional;

/**
 * @author Michael Fortin
 */
public interface Reference {
    String getName();

    String getVersion();

    String getOrg();

    String getEnv();

    Type getType();

    Level getLevel();

    BaseHierarchicalConfiguration getConfiguration();

    Optional<URL> getRemoteReference();

    String toMavenPath();

    String toMavenSnapshotMetadata();

    boolean isMavenSnapshot();

    String toSnapshotPath(String sVersion);

    enum Level {
        LOCAL,
        PROJECT,
        REMOTE
    }

    enum Type {
        PROJECT,
        ARCHETYPE,
        MODULE,
        BUNDLE
    }
}
