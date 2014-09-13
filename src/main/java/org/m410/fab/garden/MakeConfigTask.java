package org.m410.fab.garden;


import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;
import org.m410.fab.config.Module;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Map<String,Object> map = new HashMap<>();

        final Map<String, Object> app = new HashMap<>();
        app.put("properties",context.getApplication().getProperties());
        app.put("name",context.getApplication().getName());
        app.put("org",context.getApplication().getOrg());
        app.put("version",context.getApplication().getVersion());
        app.put("applicationClass",context.getApplication().getApplicationClass());
        app.put("description",context.getApplication().getDescription());
        app.put("authors",context.getApplication().getAuthors());

        map.put("application", app);
        final ArrayList<Map<String, Object>> modules = new ArrayList<>();
        context.getModules().stream().forEach(module ->{
            final HashMap<String, Object> modMap = new HashMap<>();
            modMap.putAll(module.getProperties());
            modMap.put("name", module.getName());
            modMap.put("org", module.getOrg());
            modMap.put("version", module.getVersion());
            modules.add(modMap);
        });
        map.put("modules", modules);

        FileSystems.getDefault().getPath(context.getBuild().getSourceOutputDir()).toFile().mkdirs();

        final Path path = FileSystems.getDefault().getPath(context.getBuild().getSourceOutputDir(),"configuration.m410.yml");
        Writer writer = new FileWriter(path.toFile());
        new Yaml().dump(map,writer);
        writer.close();
    }
}
