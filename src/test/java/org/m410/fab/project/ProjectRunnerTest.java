package org.m410.fab.project;

import org.junit.Test;
import org.m410.fab.Application;
import org.m410.fab.project.BuildConfig;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author m410
 */
public class ProjectRunnerTest {

    @Test
    public void testLoadConfig() throws Exception {
        final String path = "/Users/m410/Projects/fabricate/fab-cli/src/test/resources/test-project.fab.yml";
        File file = FileSystems.getDefault().getPath(path).toFile();
        assertTrue("sample config not found",file.exists());
        BuildConfig config = new ProjectRunner(Arrays.asList("build")).loadLocalConfig(file);
        assertNotNull("no config created", config);

        assertNotNull("no archetype", config.getArchetype());
        assertNotNull("no org", config.getArchetype().getOrganization());
        assertEquals("org.m410.fabricate.garden", config.getArchetype().getOrganization());

        assertNotNull("no version", config.getArchetype().getVersion());
        assertEquals("0.1-SNAPSHOT", config.getArchetype().getVersion());

        assertNotNull("no bundles", config.getBundles());
        assertEquals(1, config.getBundles().size());

        assertNotNull("no persistence", config.getPersistence());
        assertEquals(1, config.getPersistence().size());

        assertNotNull("no modules", config.getModules());
        assertEquals(1, config.getModules().size());
    }
}
