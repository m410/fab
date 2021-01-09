package org.m410.fabricate.service.internal.task;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;


/**
 * @author m410
 */
public class LocalPublishTask implements Task {

    @Override
    public String getName() {
        return "local-publish";
    }

    @Override
    public String getDescription() {
        return "Publish to local maven repository";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        context.cli().debug("publish local!!!");
    }
}
