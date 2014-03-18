package org.m410.fab.config;

/**
 * @author m410
 */
public interface Module {

    static enum Type {
        Persistence,
        View,
        Runtime
    }

    Type type();

    String name();

    String organization();

    String version();
}
