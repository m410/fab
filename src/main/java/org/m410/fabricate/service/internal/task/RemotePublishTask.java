package org.m410.fabricate.service.internal.task;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

/**
 * @author m410
 */
public class RemotePublishTask implements Task {
    @Override
    public String getName() {
        return "remote-publish";
    }

    @Override
    public String getDescription() {
        return "Publish artifacts to remote maven repository";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        context.cli().debug("remote publish!!!");
    }
}
