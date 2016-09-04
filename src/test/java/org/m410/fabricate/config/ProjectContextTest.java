package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import org.m410.config.YamlConfiguration;

import java.io.IOException;
import java.io.StringWriter;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Michael Fortin
 */
public class ProjectContextTest {

    @Test
    public void testMake() throws ConfigurationException, IOException {
        final ImmutableHierarchicalConfiguration config = Fixtures.config();
//        final StringWriter file = new StringWriter();
//        ((YamlConfiguration) config).write(file);
//        System.out.println(file.toString());
//
//        IntStream.range(0, config.getMaxIndex("dependencies") + 1).forEach(i -> {
//            System.out.println(i + ", " + config.getString("dependencies(" + i + ").name"));
//        });

        final ProjectContext projectContext = new ProjectContext(config);
        assertNotNull(projectContext);
        assertNotNull(projectContext.getConfiguration());
        assertNotNull(projectContext.getApplication());
        assertNotNull(projectContext.getBuild());
        assertNotNull(projectContext.getDependencies());
        assertNotNull(projectContext.getModules());
        assertEquals(3, projectContext.getModules().size());
        assertEquals(7, projectContext.getDependencies().size());
    }
}