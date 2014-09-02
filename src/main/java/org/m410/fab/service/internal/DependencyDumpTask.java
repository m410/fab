package org.m410.fab.service.internal;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;
import org.m410.fab.config.Dependency;


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

        for (Dependency dependency : context.getDependencies()) {
            context.cli().println(dependency.toString());
        }

        context.cli().println("");

        for (String s : context.getClasspath().keySet()) {
            context.cli().println(s + " - " + context.getClasspath().get(s));
        }

    }
}
