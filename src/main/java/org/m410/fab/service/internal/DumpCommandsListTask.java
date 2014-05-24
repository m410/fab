package org.m410.fab.service.internal;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

/**
 * @author m410
 */
public class DumpCommandsListTask implements Task {
    @Override
    public String getName() {
        return "dump-commands";
    }

    @Override
    public String getDescription() {
        return "Dump the list of commands to standard out";
    }

    @Override
    public void execute(BuildContext context) {
        context.cli().debug("dump commands!!!");
    }
}
