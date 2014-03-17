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
        context.cli().debug("COMPILING BITCHES!!!");
    }
}
