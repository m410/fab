package org.m410.fab.task;

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
    public void execute(BuildContext context) {
        context.cli().debug("info!!!");
    }
}
