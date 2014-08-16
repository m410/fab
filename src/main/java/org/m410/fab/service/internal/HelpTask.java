package org.m410.fab.service.internal;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

/**
 * @author m410
 */
public class HelpTask implements Task {
    @Override
    public String getName() {
        return "Help";
    }

    @Override
    public String getDescription() {
        return "Display Help Information";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        context.cli().println("Display help information");
    }
}
