package org.m410.fab.config;

import java.io.Serializable;
import java.util.Map;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface Application extends Serializable {
    String getName();

    String getOrg();

    String getVersion();

    Map<String, Object> getProperties();

    String getApplicationClass();

    String getAuthors();

    String getDescription();
}
