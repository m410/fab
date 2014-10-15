package org.m410.fabricate.project;

import org.junit.Test;
import org.m410.fabricate.config.ProjectConfig;

import java.io.File;
import java.nio.file.FileSystems;

import static org.junit.Assert.*;

/**
 * @author m410
 */
public class ProjectRunnerTest {

    @Test
    public void testLoadConfig() throws Exception {
        final String path = "src/test/resources/test-project.fab.yml";
        File file = FileSystems.getDefault().getPath(path).toFile();
        assertTrue("sample config not found",file.exists());

        final File configCacheDir = FileSystems.getDefault().getPath("target/test-output").toFile();

        if(!configCacheDir.exists() && !configCacheDir.mkdirs())
            fail("could not make test output directories");

        ProjectConfig config = new ProjectConfig(file,configCacheDir);
        assertNotNull("no config created", config);

        assertNotNull("no archetype", config.getArchetype());
        assertNotNull("no org", config.getArchetype().getOrganization());
        assertEquals("org.m410.fabricate.garden", config.getArchetype().getOrganization());

        assertNotNull("no version", config.getArchetype().getVersion());
        assertEquals("0.1-SNAPSHOT", config.getArchetype().getVersion());

        assertNotNull("no bundles", config.getBundles());
        assertEquals(3, config.getBundles().size());

        assertNotNull("no modules", config.getModules());
        assertEquals(2, config.getModules().size());
    }
}
