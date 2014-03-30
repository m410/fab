package org.m410.fab;

import org.junit.Test;

import java.io.File;
import java.nio.file.FileSystems;

import static org.junit.Assert.*;

/**
 * @author m410
 */
public class ApplicationTest {

    @Test
    public void testLoadConfig() throws Exception {
        final String path = "/Users/m410/Projects/fab(ricate)/sample/garden.m410.yml";
        File file = FileSystems.getDefault().getPath(path).toFile();
        assertTrue("sample config not found",file.exists());
        BuildConfig config = Application.loadLocalConfig(file);
        assertNotNull("no config created", config);
        assertNotNull("no url", config.getUrl());
        assertNotNull("no org", config.getBuild_org());
        assertEquals("org.m410.fab", config.getBuild_org());
        assertNotNull("no version", config.getBuild_version());
        assertEquals("0.1.0", config.getBuild_version());
        assertNotNull("no bundles", config.getBundles());
        assertEquals(1, config.getBundles().size());
        assertNotNull("no persistence", config.getPersistence());
        assertEquals(1, config.getPersistence().size());
        assertNotNull("no modules", config.getModules());
        assertEquals(1, config.getModules().size());
    }
}
