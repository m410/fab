package org.m410.fab.garden;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

/**
 * @author m410
 */
public class JUnitTestRunnerTask implements Task {
    @Override
    public String getName() {
        return "JUnit Test Runner";
    }

    @Override
    public String getDescription() {
        return "Run all JUnit unit tests";
    }

    @Override
    public void execute(BuildContext context) throws Exception {

    }
}
