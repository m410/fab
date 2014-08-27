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

        // create a new classloader with the test classpath
        System.out.println("this.class.classLoader: " + this.getClass().getClassLoader());
        System.out.println("this.class.classLoader.parent: " + this.getClass().getClassLoader().getParent());
        System.out.println("Thread.currentThread.contextClassLoader: " + Thread.currentThread().getContextClassLoader());
        System.out.println("Thread.currentThread.contextClassLoader.parent: " + Thread.currentThread().getContextClassLoader().getParent());
        // add classes and test classes directories
        // check source for Test classes
        // via reflection get a reference to those classes
        // call org.junit.runner.JUnitCore.runClasses(<array of classes>)
        // http://junit.sourceforge.net/javadoc/


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
