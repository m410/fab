package org.m410.fab.task;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

/**
 * @author m410
 */
public class DumpTaskListTask implements Task {
    @Override
    public String getName() {
        return "dump-tasks";
    }

    @Override
    public String getDescription() {
        return "Dump the list of tasks to standard out";
    }

    @Override
    public void execute(BuildContext context) {
        context.cli().debug("tasks!!!");
    }
}
