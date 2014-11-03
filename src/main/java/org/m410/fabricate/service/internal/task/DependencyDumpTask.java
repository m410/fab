package org.m410.fabricate.service.internal.task;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;
import org.m410.fabricate.config.Dependency;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;


/**
 * @author m410
 */
public class DependencyDumpTask implements Task {
    @Override
    public String getName() {
        return "output-dependencies";
    }

    @Override
    public String getDescription() {
        return "Output Dependencies";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        DumperOptions options = new DumperOptions();
        Representer representer = new Representer();
        representer.addClassTag(Dependency.class, Tag.MAP);

        final Yaml yaml = new Yaml(representer,options);
        final HashMap<String, List<Dependency>> map = new HashMap<>();
        map.put("dependencies",context.getDependencies());
        context.cli().println(yaml.dump(map));

        for (String s : context.getClasspath().keySet()) {
            context.cli().println("");
            context.cli().println("classpath:"+s);
            context.cli().println(context.getClasspath().get(s));
        }

    }
}
