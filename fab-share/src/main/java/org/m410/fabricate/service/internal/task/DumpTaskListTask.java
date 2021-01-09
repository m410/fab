package org.m410.fabricate.service.internal.task;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

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
    public void execute(BuildContext context) throws Exception {
        context.cli().debug("tasks!!!");
    }
}
