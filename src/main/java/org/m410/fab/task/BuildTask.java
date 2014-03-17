package org.m410.fab.task;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

/**
 * @author m410
 */
public class BuildTask implements Task {
    @Override
    public String getName() {
        return "build task";
    }

    @Override
    public String getDescription() {
        return "Build the project";
    }

    @Override
    public void execute(BuildContext context) {
        context.cli().debug("BUILDING BITCHES!!!");
    }
}