package org.m410.fabricate.service.internal.task;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;


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
        context.getDependencies().forEach(d -> context.cli().println(d.toString()));

        for (String s : context.getClasspath().keySet()) {
            context.cli().println("");
            context.cli().println("classpath:"+s);
            context.cli().println(context.getClasspath().get(s));
        }

    }
}
