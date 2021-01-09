package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.io.Serializable;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
// todo replace with just the instance
public interface Application extends Serializable {
    String getName();

    String getOrg();

    String getVersion();

    ImmutableHierarchicalConfiguration getProperties();

    String getApplicationClass();

    String getAuthors();

    String getDescription();
}
