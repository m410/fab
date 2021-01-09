package org.m410.fabricate.junit;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        final ClassLoader parent = Thread.currentThread().getContextClassLoader().getParent();
        List<URL> urls = makeClasspath(context);
        context.cli().debug("urls:" + urls);

        ClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), parent);
        List<String> classes = findTestClasses(context);

        Class<?> runnerClass = classLoader.loadClass("org.junit.runner.JUnitCore");
        Class<?> resultClass = classLoader.loadClass("org.junit.runner.Result");

        Object runnerInstance = runnerClass.newInstance();
        Method runClasses = runnerClass.getMethod("runClasses", Class[].class);

        context.cli().debug("test classes:" + classes);
        context.cli().debug("runner:" + runClasses);
        context.cli().debug("runnerInstance:" + runnerInstance);

        for (String aClass : classes) {
            Class testClass = classLoader.loadClass(aClass);
            Object result = runClasses.invoke(runnerInstance, new Object[]{new Class[]{testClass}});
            Method wasSuccessful = resultClass.getMethod("wasSuccessful");

            if(!(Boolean)wasSuccessful.invoke(result)) {
                Method failureMethod = resultClass.getMethod("getFailures");
                List failures = (List)failureMethod.invoke(result);

                for (Object failure : failures)
                    context.cli().error(failure.toString());
            }
        }
    }

    private static List<String> findTestClasses(BuildContext context) throws IOException {
        Path path = FileSystems.getDefault().getPath(context.getConfiguration().getString("build.test_dir"));
        final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*Test.java");
        return Files.walk(path).filter(matcher::matches)
                .map(Path::toFile)
                .map(File::getAbsolutePath)
                .map(s->s.substring(path.toFile().getAbsolutePath().length() + 1))
                .map(s->s.replace(".java",""))
                .map(s -> s.replaceAll("[\\\\/]", "."))
                .collect(Collectors.toList());
    }

    private static List<URL> makeClasspath(BuildContext context) throws MalformedURLException {
        final ArrayList<URL> urls = new ArrayList<>();
        List<String> paths = toPaths(context.getClasspath().get("test"));

        for (String path : paths)
            urls.add(asUrl(path));

        urls.add(asUrl(context.getConfiguration().getString("build.source_output_dir")));
        urls.add(asUrl(context.getConfiguration().getString("build.test_output_dir")));
        return urls;
    }

    private static List<String> toPaths(String test) {
        if(test != null)
            return Arrays.asList(test.split(System.getProperty("path.separator")));
        else
            return new ArrayList<>();
    }

    private static URL asUrl(String sourceOutputDir) throws MalformedURLException {
        return FileSystems.getDefault().getPath(sourceOutputDir).toFile().toURI().toURL();
    }
}
