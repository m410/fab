package org.m410.fab.garden;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

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
        context.cli().println("urls:" + urls);

        ClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), parent);
        List<String> classes = findTestClasses(context);

        Class runnerClass = classLoader.loadClass("org.junit.runner.JUnitCore");
        Object runnerInstance = runnerClass.newInstance();
        Method runClasses = runnerClass.getMethod("runClasses", Class[].class);

        context.cli().println("test classes:" + classes);
        context.cli().println("runner:" + runClasses);
        context.cli().println("runnerInstance:" + runnerInstance);

        for (String aClass : classes) {
            Class testClass = classLoader.loadClass(aClass);
            Object result = runClasses.invoke(runnerInstance, new Object[]{new Class[]{testClass}});
            context.cli().println(result.toString());
            // for (Failure failure : result.getFailures())
            //    System.out.println(failure.toString());
        }
    }

    private static List<String> findTestClasses(BuildContext context) throws IOException {
        Path path = FileSystems.getDefault().getPath(context.build().getTestDir());
        final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*Test.java");
        return Files.walk(path).filter(matcher::matches)
                .map(Path::toFile)
                .map(File::getAbsolutePath)
                .map(s->s.substring(path.toFile().getAbsolutePath().length()))
                .map(s->s.replaceAll("[\\\\/]", "."))
                .collect(Collectors.toList());
    }

    private static List<URL> makeClasspath(BuildContext context) throws MalformedURLException {
        final ArrayList<URL> urls = new ArrayList<>();
        List<String> paths = toPaths(context.classpaths().get("test"));

        for (String path : paths)
            urls.add(asUrl(path));

        urls.add(asUrl(context.build().getSourceOutputDir()));
        urls.add(asUrl(context.build().getTestOutputDir()));
        return urls;
    }

    private static List<String> toPaths(String test) {
        return Arrays.asList(test.split(System.getProperty("path.separator")));
    }

    private static URL asUrl(String sourceOutputDir) throws MalformedURLException {
        return FileSystems.getDefault().getPath(sourceOutputDir).toFile().toURI().toURL();
    }
}
