package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import org.m410.config.YamlConfiguration;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 * @author Michael Fortin
 */
public class ApplicationImplTest {

    @Test
    public void testMakeApplication() throws ConfigurationException, IOException {
        ImmutableHierarchicalConfiguration config = Fixtures.config();
        Application application = new ApplicationImpl(config.immutableConfigurationAt("application"));
        assertNotNull(application);
        assertNotNull(application.getName());
        assertNotNull(application.getOrg());
        assertNotNull(application.getVersion());
        assertNotNull(application.getApplicationClass());
        assertNotNull(application.getAuthors());
        assertNotNull(application.getDescription());

        // todo fix
        assertNull(application.getProperties());
    }
}