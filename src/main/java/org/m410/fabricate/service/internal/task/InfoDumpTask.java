package org.m410.fabricate.service.internal.task;

import org.m410.config.YamlConfiguration;
import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

import java.io.StringWriter;

/**
 * @author m410
 */
public class InfoDumpTask implements Task {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Dump project properties to standard out";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        final YamlConfiguration configuration = (YamlConfiguration) context.getConfiguration();

        try (StringWriter out = new StringWriter()) {
            configuration.write(out);
            context.cli().println(System.getProperty("line.separator") + out.toString());
        }
    }
}
