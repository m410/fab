package org.m410.fab.compiler;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

import javax.tools.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
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
public class CompileTask implements Task {
    @Override
    public String getName() {
        return "compile task";
    }

    @Override
    public String getDescription() {
        return "Compile the project";
    }

    @Override
    public void execute(BuildContext context) {
        List<String> options = new ArrayList<>();
        makeClasspathOption(context).ifPresent(options::add);
        makeSourceOption(context).ifPresent(options::add);
        makeTargetOption(context).ifPresent(options::add);
        makeOutputOption(context).ifPresent(options::add);
        makeSourcePathOption(context).ifPresent(options::add);

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
        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, stdFileManager, diagnostics, options, null, sources);
        boolean status = compilerTask.call();

        if (!status) {
            for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                System.out.format("Error on line %d in %s\n", diagnostic.getLineNumber(), diagnostic);
            }
        }

        try {
            stdFileManager.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Optional<String> makeSourcePathOption(BuildContext context) {
        return Optional.empty();
    }

    private Optional<String> makeOutputOption(BuildContext context) {
        return Optional.empty();
    }

    private Optional<String> makeTargetOption(BuildContext context) {
        return Optional.empty();
    }

    private Optional<String> makeSourceOption(BuildContext context) {
        return Optional.empty();
    }

    private Optional<String> makeClasspathOption(BuildContext context) {
        return Optional.empty();
    }

    private List<JavaFileObject> makeSources(BuildContext context) throws IOException {
        List<JavaFileObject> sources = new ArrayList<>();

        final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.java");
        final Path path = FileSystems.getDefault().getPath("src/java");
        Files.walk(path).filter(matcher::matches).forEach(p->{
            sources.add(new JavaFileObj(p.toUri(), JavaFileObj.Kind.SOURCE));
        });

        return sources;
    }
}
