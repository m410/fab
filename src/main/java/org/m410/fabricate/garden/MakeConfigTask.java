package org.m410.fabricate.garden;


import org.m410.config.YamlConfig;
import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        YamlConfig.write(context.getConfiguration(), path.toFile());
    }
}
