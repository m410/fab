package org.m410.fabricate;

import org.apache.commons.cli.*;
import org.m410.fabricate.global.GlobalRunner;
import org.m410.fabricate.project.ProjectRunner;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * @author m410
 */
public final class Main {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Throwable {
        Options options = new Options();
        options.addOption("debug", false, "Debug output");
        options.addOption("help", false, "Display help information");
        options.addOption("version", false, "Display version information");
        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse( options, args);

        if(cmd.hasOption("version")) {
            System.out.println("display version info");
        }
        else if(cmd.hasOption("help")) {
            new HelpFormatter().printHelp("fab", options, true);
            new GlobalRunner(cmd.getArgList()).outputCommands();
        }
        else if(cmd.getArgList().size() > 0 && isGlobal(cmd.getArgList().get(0).toString())) {
            runGlobalCmd(cmd.getArgList());
        }
        else if(isProjectDir(System.getProperty("user.dir"))) {
            runProjectCmd(cmd.getArgList(),cmd.hasOption("debug"));
        }
        else {
            System.out.println("The current directory is not a project directory or unknown command.  Try 'fab -help'");
        }
    }

    protected static void runProjectCmd(List<String> argList, boolean debug) throws Throwable {
        new ProjectRunner(argList, debug).run();
    }

    protected static void runGlobalCmd(List<String> argList) throws Exception {
        new GlobalRunner(argList).run();
    }

    protected static boolean isProjectDir(String userDir) throws IOException {
        final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.fab.yml");
        final Path path = FileSystems.getDefault().getPath(userDir);
        return Files.walk(path, 1).filter(matcher::matches).findFirst().isPresent();
    }

    protected static boolean isGlobal(String s) {
        return GlobalRunner.isGlobalCommand(s);
    }
}
