package org.m410.fabricate.config;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

import static org.junit.Assert.*;

public class ProjectConfigTest {

    @Test
    public void testCreate() throws IOException, ConfigurationException {
        File file = FileSystems.getDefault().getPath("src/test/resources/test-project.fab.yml").toFile();
        final File configCacheDir = FileSystems.getDefault().getPath("target/test-output").toFile();

        if(!configCacheDir.exists() && !configCacheDir.mkdirs())
            fail("could not make test output directories");

        assertNotNull(configCacheDir);
        Project config = new Project(file, configCacheDir, "default");
        assertNotNull(config);
        assertEquals(5, config.getReferences().size());
        assertEquals(4, config.getBundles().size());
        assertEquals(3, config.getModuleBaseReferences().size());
        assertNotNull(config.getArchetypeReference());
    }

}