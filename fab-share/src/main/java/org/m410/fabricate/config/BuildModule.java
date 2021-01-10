package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableConfiguration;

import java.util.Map;

/**
 * @author m410
 */
// todo replace with just the instance
public interface BuildModule {

    enum Type {
        Persistence,
        Views,
        Modules,
        Logging,
        Testing;

        public static Type of(final String x) {
            switch (x.toLowerCase().trim()) {
                case "persistence":
                    return Persistence;
                case "views":
                    return Views;
                case "testing":
                    return Testing;
                case "logging":
                    return Logging;
                case "modules":
                    return Modules;
                default:
                    throw new IllegalArgumentException("No type of: " + x);
            }
        }
    }

    Type getType();

    String getName();

    String getOrg();

    String getVersion();

    ImmutableConfiguration getConfiguration();
}
