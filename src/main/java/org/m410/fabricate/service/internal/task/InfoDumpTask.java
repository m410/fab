package org.m410.fabricate.service.internal.task;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;
import org.m410.fabricate.config.Dependency;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.util.HashMap;
import java.util.List;

/**
 * @author m410
 */
public class InfoDumpTask implements Task {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Dump project properties to standard out";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        DumperOptions options = new DumperOptions();
        Representer representer = new Representer();
        representer.addClassTag(Dependency.class, Tag.MAP);

        final Yaml yaml = new Yaml(representer,options);
        final HashMap<String, Object> map = new HashMap<>();
        map.put("env" , context.environment());
        map.put("application" , context.getApplication());
        map.put("build" , context.getBuild());
        map.put("modules" , context.getModules());

        context.cli().println(yaml.dump(map));
    }
}
