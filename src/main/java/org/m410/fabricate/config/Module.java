package org.m410.fabricate.config;

import java.util.Map;

/**
 * @author m410
 */
public interface Module {

    static enum Type {
        Persistence,
        View,
        Runtime,
        Logger,
        Test
    }

    Type getType();

    String getName();

    String getOrg();

    String getVersion();

    Map<String,Object> getProperties();
}
