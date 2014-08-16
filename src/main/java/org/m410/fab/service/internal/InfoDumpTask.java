package org.m410.fab.service.internal;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

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
        context.cli().println("build:" + context.build());
        context.cli().println("app:" + context.application());
        context.cli().println("env:" + context.environment());
        context.cli().println("dependencies:" + context.dependencies());
        context.cli().println("modules:" + context.modules());
    }
}
