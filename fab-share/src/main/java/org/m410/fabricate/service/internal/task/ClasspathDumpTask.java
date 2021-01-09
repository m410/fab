package org.m410.fabricate.service.internal.task;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;


/**
 * @author m410
 */
public class ClasspathDumpTask implements Task {
    @Override
    public String getName() {
        return "output-classpaths";
    }

    @Override
    public String getDescription() {
        return "Output Classpaths";
    }

    @Override
    public void execute(BuildContext context) throws Exception {

        for (String s : context.getClasspath().keySet()) {
            context.cli().println("");
            context.cli().println("classpath:" + s);
            context.cli().println(context.getClasspath().get(s));
        }

    }
}
