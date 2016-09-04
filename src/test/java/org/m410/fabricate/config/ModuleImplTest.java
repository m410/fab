package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * @author Michael Fortin
 */
public class ModuleImplTest {

    @Test
    public void testMakeModules() throws ConfigurationException {
        ImmutableHierarchicalConfiguration config = Fixtures.config();
        final String name = "modules(org..m410..mod..persistence:m410-jpa:0..1)";
        ImmutableConfiguration mod = config.immutableSubset(name);

        Module module = new ModuleImpl(name, mod);
        assertNotNull(module.getName());
        assertNotNull(module.getOrg());
        assertNotNull(module.getConfiguration());
        assertNotNull(module.getType());
        assertNotNull(module.getVersion());
    }
}