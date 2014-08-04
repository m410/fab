package org.m410.fab.service;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

/**
 * @author m410
 */
public class BuildTask implements Task {
    @Override
    public String getName() {
        return "build";
    }

    @Override
    public String getDescription() {
        return "Main build command";
    }

    @Override
    public void execute(BuildContext context) {
        context.cli().debug("building");
    }
}
