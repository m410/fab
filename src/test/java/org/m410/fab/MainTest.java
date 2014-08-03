package org.m410.fab;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.junit.Assert.*;

/**
 * @author m410
 */
public class MainTest {

    @Test
    public void notProjectDirectory() throws IOException {
        String userDir = FileSystems.getDefault().getPath("").toFile().getAbsolutePath();
        assertFalse(Main.isProjectDir(userDir));
    }

    @Test
    public void projectDirectory() throws IOException {

        final Path path = FileSystems.getDefault().getPath("");
        String userDir;

        if(path.toFile().getAbsolutePath().endsWith("fab-cli"))
            userDir = FileSystems.getDefault().getPath("","src/test/resources").toFile().getAbsolutePath();
        else
            userDir = FileSystems.getDefault().getPath("","fab-cli/src/test/resources").toFile().getAbsolutePath();

        assertTrue(Main.isProjectDir(userDir));
    }
}
