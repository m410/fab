package org.m410.fabricate.config;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import org.m410.config.YamlConfig;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author Michael Fortin
 */
public class ArchetypeTest {
    @Test
    public void construct() throws ConfigurationException {
        final BaseHierarchicalConfiguration load = YamlConfig.load(new File("src/test/resources/test.yml"));
        Archetype archetype = new Archetype(load);
        assertNotNull(archetype);
        assertNotNull(archetype.getName());
        assertEquals("test-app",archetype.getName());
        assertNotNull(archetype.getRemoteReference());
        assertTrue(archetype.getRemoteReference().isPresent());
    }
}