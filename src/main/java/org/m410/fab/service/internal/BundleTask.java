package org.m410.fab.service.internal;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

/**
 * @author m410
 */
public class BundleTask implements Task {

    @Override
    public String getName() {
        return "bundle";
    }

    @Override
    public String getDescription() {
        return "Display Bundle list";
    }

    @Override
    public void execute(BuildContext context) {
        context.cli().println("bundle information");
    }
}
