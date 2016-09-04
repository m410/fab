package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Michael Fortin
 */
public class DependencyTest {

    @Test
    public void testCount() throws ConfigurationException {
        ImmutableHierarchicalConfiguration config = Fixtures.config();
        assertEquals(6, config.getMaxIndex("dependencies"));
    }
    @Test
    public void testMakeDependencies() throws ConfigurationException {
        ImmutableHierarchicalConfiguration config = Fixtures.config();

        Dependency dependency = new Dependency(config.get(Map.class,"dependencies(0)"));
        assertNotNull(dependency.getName());
        assertNotNull(dependency.getOrg());
        assertNotNull(dependency.getRev());
        assertNotNull(dependency.getScope());
        assertNotNull(dependency.isTransitive());
        assertFalse(dependency.isTransitive());
    }
}