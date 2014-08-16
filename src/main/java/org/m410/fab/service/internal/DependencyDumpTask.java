package org.m410.fab.service.internal;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;


/**
 * @author m410
 */
public class DependencyDumpTask implements Task {
    @Override
    public String getName() {
        return "output-dependencies";
    }

    @Override
    public String getDescription() {
        return "Output Dependencies";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        context.cli().debug("context: " + context);
        context.cli().debug("dependency BITCHES!!!");
    }
}
