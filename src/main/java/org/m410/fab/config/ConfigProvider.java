package org.m410.fab.config;

import java.util.Map;
import java.util.Set;

/**
 * @author m410
 */
public interface ConfigProvider {

    Map<String, Object> config();

    Set<String> validate(Map<String,Object> fullConfig);
}
