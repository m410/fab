package org.m410.fabricate.service.internal.task;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

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
    public void execute(BuildContext context) throws Exception {
        context.getConfiguration().immutableConfigurationsAt("bundles").forEach(bc -> {
            String txt = "- " + bc.getString("name") +
                         ":" + bc.getString("org") +
                         ":" + bc.getString("version");
            context.cli().println(txt);
        });
    }
}
