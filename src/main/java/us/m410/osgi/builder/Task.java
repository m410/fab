package us.m410.osgi.builder;

import java.util.Set;

/**
 * Should be immutable
 *
 * @author Michael Fortin
 */
public interface Task {
    String getName(); // must be unique
    String getDescription();
    String getHelp();
    String[] getPrerequisites();
    void execute(BuildContext context);
}
