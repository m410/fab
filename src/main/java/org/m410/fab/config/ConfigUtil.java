package org.m410.fab.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

/**
 * @author m410
 */
public class ConfigUtil {
    public static File projectConfigFile(final String userDir) throws IOException {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.fab.yml");
        Path path = FileSystems.getDefault().getPath(userDir);
        return Files.walk(path, 1).filter(matcher::matches).findFirst().get().toFile();
    }

}
