package org.m410.fabricate.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.util.Map;
import java.util.Set;

/**
 * @author m410
 */
public interface ConfigProvider {

    Configuration configuration();

    Set<String> validate(ImmutableHierarchicalConfiguration fullConfig);

    Type getType();

    String getEnvName();

    enum Type {
        LOCAL,
        PROJECT,
        MODULE,
        ARCHETYPE
    }
}
