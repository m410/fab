package org.m410.fabricate.project;

import org.junit.Test;
import org.m410.fabricate.config.Project;

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

        Project config = new Project(file,configCacheDir, "default");
        assertNotNull("no config created", config);

        assertNotNull("no archetype", config.getArchetype());
        assertNotNull("no org", config.getArchetype().getOrg());
        assertEquals("org.m410.fabricate.garden", config.getArchetype().getOrg());

        assertNotNull("no version", config.getArchetype().getVersion());
        assertEquals("0.1-SNAPSHOT", config.getArchetype().getVersion());

        assertNotNull("no modules", config.getModuleBaseReferences());
        assertEquals(3, config.getModuleBaseReferences().size());

        assertNotNull("no bundles", config.getBundles());
        assertEquals(4, config.getBundles().size());
    }
}
