package org.m410.fabricate.service.internal.task;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

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
    public void execute(BuildContext context) throws Exception {
        context.cli().debug("deploy!!!");
    }
}
