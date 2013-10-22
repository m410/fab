package us.m410.osgi.module;

import us.m410.osgi.builder.BuildContext;
import us.m410.osgi.builder.Task;

/**
 * This is a basic build task implementation.  The main work happens in the execute
 * method.  Change all values as needed
 *
 * @author Michael Fortin
 */
public class ExampleTask implements Task {

    // must be unique name, and should be lowercase, no spaces by convention.
    static final private String name = "my-task";

    // this is what
    static final private String description = "My Simple Task";
    static final private String help = "Some Help text here";

    static final private String[] prerequisites = new String[]{};

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getHelp() {
        return help;
    }

    @Override
    public String[] getPrerequisites() {
        return prerequisites;
    }

    @Override
    public void execute(BuildContext context) {
        context.cli().info("Yes, I've been called");
    }
}
