package org.m410.fabricate.garden;


import org.m410.config.YamlConfiguration;
import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

import java.io.FileWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author m410
 */
public class MakeConfigTask implements Task {
    @Override public String getName() {
        return "make-config";
    }

    @Override public String getDescription() {
        return "Creates the runtime configuration for garden";
    }

    @Override public void execute(BuildContext context) throws Exception {
        final String sourceOutputDir = context.getBuild().getSourceOutputDir();
        FileSystems.getDefault().getPath(sourceOutputDir).toFile().mkdirs();
        final Path path = FileSystems.getDefault().getPath(sourceOutputDir,"garden.fab.yml");

        try (FileWriter writer = new FileWriter(path.toFile())) {
            ((YamlConfiguration) context.getConfiguration()).write(writer);
        }
    }
}
