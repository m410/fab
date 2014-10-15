package org.m410.fabricate.compiler;

import org.m410.fabricate.builder.BuildContext;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author m410
 */
public final class ContextJavaCompiler {
    private final boolean testCompile;
    private final ArrayList<String> options;
    private final List<JavaFileObject> sources;

    private final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.java");

    public ContextJavaCompiler(File source, File output, String compilePath) throws IOException {
        testCompile = false;
        this.options = new ArrayList<>();
        this.sources = new ArrayList<>();

        options.add("-cp");
        options.add(compilePath);

        options.add("-d");
        options.add(output.getAbsolutePath());

        options.add("-sourcepath");
        options.add(source.getAbsolutePath());

        Files.walk(source.toPath()).filter(matcher::matches).forEach(p->{
            final URI uri = p.toUri();
            sources.add(new JavaFileObj(uri, JavaFileObj.Kind.SOURCE));
        });
    }

    public ContextJavaCompiler(BuildContext context, boolean testCompile) throws IOException {
        context.cli().debug("testCompile:"+testCompile);
        this.testCompile = testCompile;
        this.options = new ArrayList<>();

        makeClasspathOption(context).ifPresent(options::addAll);
        makeSourceOption(context).ifPresent(options::addAll);
        makeTargetOption(context).ifPresent(options::addAll);
        makeOutputOption(context).ifPresent(options::addAll);
        makeSourcePathOption(context).ifPresent(options::addAll);

        sources = makeSources(context);

        context.cli().debug("options:" + options);
        context.cli().debug("sources:" + sources);
    }

    public Status compile() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null, Locale.getDefault(), null);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, stdFileManager, diagnostics, options, null, sources);
        boolean status = compilerTask.call();

        try {
            stdFileManager.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return new Status(status, diagnostics);
    }

    public final class Status {
        private final boolean ok;
        private String message;

        public Status(boolean status, DiagnosticCollector<JavaFileObject> diagnostics) {
            this.ok = status;

            if (!status) {
                StringBuilder sb = new StringBuilder();
                for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                    System.out.format("Error on line %d in %s\n", diagnostic.getLineNumber(), diagnostic);
                    sb.append(diagnostic.getMessage(Locale.getDefault()));
                }
                message = sb.toString();
            }
        }

        public boolean isOk() {
            return ok;
        }
    }

    private Optional<List<String>> makeSourcePathOption(BuildContext context) {
        final String sourceDir = testCompile
                ? context.getBuild().getTestDir()
                : context.getBuild().getSourceDir();

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
                ? context.getBuild().getTestOutputDir()
                : context.getBuild().getSourceOutputDir();
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
        final String outputDir = context.getBuild().getSourceOutputDir();
        final String path = FileSystems.getDefault().getPath(outputDir).toFile().getAbsolutePath();
        return path + System.getProperty("path.separator");
    }

    private List<JavaFileObject> makeSources(BuildContext context) throws IOException {
        List<JavaFileObject> sources = new ArrayList<>();

        final Path path = testCompile
                ? FileSystems.getDefault().getPath(context.getBuild().getTestDir())
                : FileSystems.getDefault().getPath(context.getBuild().getSourceDir());

        Files.walk(path).filter(matcher::matches).forEach(p->{
            final URI uri = p.toUri();
            context.cli().debug("add source:" + uri);
            sources.add(new JavaFileObj(uri, JavaFileObj.Kind.SOURCE));
        });

        return sources;
    }
}
