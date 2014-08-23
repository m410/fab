package org.m410.fab.javalib;

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
//        List testCases = new ArrayList();
//        testCases.add(TestFeatureOne.class);
//        testCases.add(TestFeatureTwo.class);
//        for (Class testCase : testCases)
//            runTestCase(testCase);
//        Result result = JUnitCore.runClasses(testCase);
//        for (Failure failure : result.getFailures())
//            System.out.println(failure.toString());
//
    }
}
