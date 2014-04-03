package org.m410.fab.task;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

/**
 * @author m410
 */
public class DeployTask implements Task {

    @Override
    public String getName() {
        return "deploy";
    }

    @Override
    public String getDescription() {
        return "Deploy Artifacts";
    }

    @Override
    public void execute(BuildContext context) {
        context.cli().debug("deploy!!!");
    }
}
