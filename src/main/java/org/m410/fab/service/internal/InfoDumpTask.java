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
        context.cli().println("build:" + context.getBuild());
        context.cli().println("app:" + context.getApplication());
        context.cli().println("env:" + context.environment());
        context.cli().println("dependencies:" + context.getDependencies());
        context.cli().println("modules:" + context.getModules());
    }
}
