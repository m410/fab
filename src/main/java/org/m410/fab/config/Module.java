package org.m410.fab.config;

import java.util.Map;

/**
 * @author m410
 */
public interface Module {

    static enum Type {
        Persistence,
        View,
        Runtime,
        Logger
    }

    Type getType();

    String getName();

    String getOrg();

    String getVersion();

    Map<String,Object> getProperties();
}
