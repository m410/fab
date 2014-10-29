package org.m410.fabricate.config;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

import static org.junit.Assert.*;

public class ProjectConfigTest {
    @Test
    public void testCreate() throws IOException {
        File file = FileSystems.getDefault().getPath("src/test/resources/test-project.fab.yml").toFile();
        final File configCacheDir = FileSystems.getDefault().getPath("target/test-output").toFile();

        if(!configCacheDir.exists() && !configCacheDir.mkdirs())
            fail("could not make test output directories");

        assertNotNull(configCacheDir);
        ProjectConfig config = new ProjectConfig(file, configCacheDir);
        assertNotNull(config);
        assertEquals(4, config.getBundles().size());
        assertEquals(3, config.getModules().size());
        assertEquals(5, config.getConfigurations().size());
        assertNotNull(config.getParent());
    }

}