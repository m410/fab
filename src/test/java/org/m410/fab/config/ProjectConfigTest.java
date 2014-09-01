package org.m410.fab.config;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

import static org.junit.Assert.*;

public class ProjectConfigTest {
    @Test
    public void testCreate() throws IOException {
        File file = FileSystems.getDefault().getPath("src/test/resources/test-project.fab.yml").toFile();
        ProjectConfig config = new ProjectConfig(file);
        assertNotNull(config);
        assertEquals(3, config.getBundles().size());
        assertEquals(2, config.getModules().size());
        assertEquals(4, config.getConfigurations().size());
        assertNotNull(config.getParent());
    }

}