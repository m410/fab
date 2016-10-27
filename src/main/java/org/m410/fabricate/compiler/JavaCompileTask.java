package org.m410.fabricate.compiler;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 *
 * http://docs.oracle.com/javase/8/docs/api/javax/tools/JavaCompiler.html
 *
 * java compiler options reference
 *
 *  -g                       Generate all debugging info
 *  -g:none                  Generate no debugging info
 *  -g:{lines, vars, source} Generate only some debugging info
 *  -nowarn                  Generate no warnings
 *  -verbose                 Output messages about what the compiler is doing
 *  -deprecation             Output source locations where deprecated APIs are used
 *  -classpath <path>        Specify where to find user class files
 *  -cp <path>               Specify where to find user class files
 *  -sourcepath <path>       Specify where to find input source files
 *  -bootclasspath <path>    Override location of bootstrap class files
 *  -extdirs <dirs>          Override location of installed extensions
 *  -endorseddirs <dirs>     Override location of endorsed standards path
 *  -d <directory>           Specify where to place generated class files
 *  -encoding <encoding>     Specify character encoding used by source files
 *  -source <release>        Provide source compatibility with specified release
 *  -target <release>        Generate class files for specific VM version
 *  -version                 Version information
 *  -help                    Print a synopsis of standard options
 *  -X                       Print a synopsis of nonstandard options
 *  -J<flag>                 Pass <flag> directly to the runtime system
 *
 * @author m410
 */
public final class JavaCompileTask implements Task {
    private boolean testCompile = false;
    public static final boolean COMPILE_SRC = false;
    public static final boolean COMPILE_TEST = true;

    public JavaCompileTask(boolean testCompile) {
        this.testCompile = testCompile;
    }

    @Override
    public String getName() {
        return "compile task";
    }

    @Override
    public String getDescription() {
        return "Compile the project";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        context.cli().debug("testCompile:"+testCompile);

        Paths.get(context.getConfiguration().getString("build.testOutputDir")).toFile().mkdirs();
        Paths.get(context.getConfiguration().getString("build.sourceOutputDir")).toFile().mkdirs();

        ArrayList<String> options = new ArrayList<>();
        makeClasspathOption(context).ifPresent(options::addAll);
        makeSourceOption(context).ifPresent(options::addAll);
        makeTargetOption(context).ifPresent(options::addAll);
        makeOutputOption(context).ifPresent(options::addAll);
        makeSourcePathOption(context).ifPresent(options::addAll);

        List<JavaFileObject> sources = null;

        try {
            sources = makeSources(context);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null, Locale.getDefault(), null);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        context.cli().debug("options:" + options);
        context.cli().debug("sources:" + sources);
        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, stdFileManager, diagnostics, options, null, sources);
        boolean status = compilerTask.call();

        if (!status)
            for (Diagnostic diagnostic : diagnostics.getDiagnostics())
                System.out.format("Error on line %d in %s\n", diagnostic.getLineNumber(), diagnostic);
            
        try {
            stdFileManager.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Optional<List<String>> makeSourcePathOption(BuildContext context) {
        final String sourceDir = testCompile
                                 ? context.getConfiguration().getString("build.testDir")
                                 : context.getConfiguration().getString("build.sourceDir");

        final File file = FileSystems.getDefault().getPath(sourceDir).toFile();

        if(!file.exists() && !file.mkdirs())
            throw new RuntimeException("Could not make source dir");

        ArrayList<String> list = new ArrayList<>(2);
        list.add("-sourcepath");
        list.add(file.getAbsolutePath());
        return Optional.of(list);
    }

    private Optional<List<String>> makeOutputOption(BuildContext context) {
        final String outputDir = testCompile
                                 ? context.getConfiguration().getString("build.testOutputDir")
                                 : context.getConfiguration().getString("build.sourceOutputDir");
        final File file = FileSystems.getDefault().getPath(outputDir).toFile();

        if(!file.exists() && !file.mkdirs())
            throw new RuntimeException("Could not make classes dir");

        ArrayList<String> list = new ArrayList<>(2);
        list.add("-d");
        list.add(file.getAbsolutePath());
        return Optional.of(list);
    }

    private Optional<List<String>> makeTargetOption(BuildContext context) {
        return Optional.empty();
    }

    private Optional<List<String>> makeSourceOption(BuildContext context) {
        return Optional.empty();
    }

    private Optional<List<String>> makeClasspathOption(BuildContext context) {
        context.cli().info(context.getClasspath().toString());

        String path = testCompile
                ? classes(context) + context.getClasspath().get("test")
                : context.getClasspath().get("compile");

        ArrayList<String> list = null;

        if(path != null){
            list = new ArrayList<>(2);
            list.add("-cp");
            list.add(path);
        }

        return Optional.ofNullable(list);
    }

    private String classes(BuildContext context) {
        final String outputDir = context.getConfiguration().getString("build.sourceOutputDir");
        final String path = FileSystems.getDefault().getPath(outputDir).toFile().getAbsolutePath();
        return path + System.getProperty("path.separator");
    }

    private List<JavaFileObject> makeSources(BuildContext context) throws IOException {
        List<JavaFileObject> sources = new ArrayList<>();
        final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.java");

        final Path path = testCompile
                          ? Paths.get(context.getConfiguration().getString("build.testDir"))
                          : Paths.get(context.getConfiguration().getString("build.sourceDir"));

        Files.walk(path).filter(matcher::matches).forEach(p->{
            final URI uri = p.toUri();
            context.cli().debug("add source:" + uri);
            sources.add(new JavaFileObj(uri, JavaFileObj.Kind.SOURCE));
        });

        return sources;
    }
}
