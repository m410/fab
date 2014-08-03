package org.m410.fab;

import org.apache.commons.cli.*;
import org.m410.fab.global.GlobalRunner;
import org.m410.fab.project.ProjectRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * @author m410
 */
public final class Main {

    public static void main(String[] args) throws Throwable {
        Options options = new Options();
        options.addOption("help", false, "Display help information");
        options.addOption("version", false, "Display version information");
        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse( options, args);

        if(cmd.hasOption("version"))
            System.out.println("display version info");

        else if(cmd.hasOption("help"))
            new HelpFormatter().printHelp("fab", options, true);

        else if(cmd.getArgList().size() > 0 && isGlobal(cmd.getArgList().get(0).toString()))
            runGlobalCmd(cmd.getArgList());

        else if(isProjectDir(System.getProperty("user.dir")))
            runProjectCmd(cmd.getArgList());

        else
            System.out.println("The current directory is not a project directory or unknown command.  Try 'fab -help'");
    }

    protected static void runProjectCmd(List<String> argList) throws Throwable {
        new ProjectRunner(argList).run();
    }

    protected static void runGlobalCmd(List<String> argList) {
        new GlobalRunner(argList).run();
    }

    protected static boolean isProjectDir(String userDir) throws IOException {
        final String pattern = "**/*.fab.yml";
        final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        final Path path = FileSystems.getDefault().getPath(userDir);
        final File file = path.toFile();
        boolean dir = file.isDirectory();

        return Files.walk(path, 1).filter((p) -> {
            final boolean matched = matcher.matches(p);
            return matched;
        }).findFirst().isPresent();
    }

    protected static boolean isGlobal(String s) {
        return GlobalRunner.isGlobalCommand(s);
    }

    static class Walker extends SimpleFileVisitor<Path> {

    }
}
