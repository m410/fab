package org.m410.fab.compiler;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

/**
 * @author m410
 */
public class CompileTask implements Task {
    @Override
    public String getName() {
        return "compile task";
    }

    @Override
    public String getDescription() {
        return "Compile the project";
    }

    @Override
    public void execute(BuildContext context) {
        context.cli().debug("build:" + context.build());
        context.cli().debug("app:" + context.application());
        context.cli().debug("env:" + context.environment());
        context.cli().debug("env:" + context.dependencies());
        context.cli().debug("env:" + context.modules());
        context.cli().debug("COMPILING BITCHES!!!");
    }
}
