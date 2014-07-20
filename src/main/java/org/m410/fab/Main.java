package org.m410.fab;

import org.apache.commons.cli.*;
import org.m410.fab.global.GlobalRunner;
import org.m410.fab.project.ProjectRunner;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
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

    private static void runProjectCmd(List<String> argList) throws Throwable {
        new ProjectRunner(argList).run();
    }

    private static void runGlobalCmd(List<String> argList) {
        new GlobalRunner(argList).run();
    }

    private static boolean isProjectDir(String property) {
        String pattern = "*.fab.yml";
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        Path path = FileSystems.getDefault().getPath(property);
        return matcher.matches(path);
    }

    private static boolean isGlobal(String s) {

        return GlobalRunner.isGlobalCommand(s);
    }
}
