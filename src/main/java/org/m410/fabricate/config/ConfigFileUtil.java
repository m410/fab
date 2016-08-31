package org.m410.fabricate.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

/**
 * Utility methods to load configuration files.
 *
 * @author m410
 */
public class ConfigFileUtil {
    public static File projectConfigFile(final String userDir) throws IOException {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.fab.yml");
        Path path = FileSystems.getDefault().getPath(userDir);
        return Files.walk(path, 1).filter(matcher::matches).findFirst().get().toFile();
    }

    public static File projectConfCache(String property) {
        // todo fix hardcode
        return FileSystems.getDefault().getPath("",".fab/config").toFile();
    }
}
