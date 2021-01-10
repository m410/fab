package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author Michael Fortin
 */
public class BuildModuleImplTest {

    @Test
    public void testMakeModules() throws ConfigurationException {
        ImmutableHierarchicalConfiguration config = Fixtures.config();
        final String name = "modules(org..m410..mod..persistence:m410-jpa:0..1)";
        ImmutableConfiguration mod = config.immutableSubset(name);

        BuildModule buildModule = new BuildModuleImpl(name, mod);
        assertNotNull(buildModule.getName());
        assertNotNull(buildModule.getOrg());
        assertNotNull(buildModule.getConfiguration());
        assertNotNull(buildModule.getType());
        assertNotNull(buildModule.getVersion());
    }

    @Test
    public void testMakeModulesAltName() throws ConfigurationException {
        ImmutableHierarchicalConfiguration config = Fixtures.config();
        final String name = "testing(org..m410..fabricate:fab-junit:0..2-SNAPSHOT)";
        ImmutableConfiguration mod = config.immutableSubset(name);

        BuildModule buildModule = new BuildModuleImpl(name, mod);
        assertNotNull(buildModule.getName());
        assertNotNull(buildModule.getOrg());
        assertNotNull(buildModule.getConfiguration());
        assertNotNull(buildModule.getType());
        assertNotNull(buildModule.getVersion());
    }
}