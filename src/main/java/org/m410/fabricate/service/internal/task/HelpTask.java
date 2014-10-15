package org.m410.fabricate.service.internal.task;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

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
