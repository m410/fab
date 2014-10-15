package org.m410.fabricate.service;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

/**
 * @author m410
 */
@Deprecated
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
    public void execute(BuildContext context) throws Exception {
        context.cli().debug("building");
    }
}
