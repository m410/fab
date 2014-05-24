package org.m410.fab.service.internal;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

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
    public void execute(BuildContext context) {
        context.cli().debug("remote publish!!!");
    }
}
