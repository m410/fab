package org.m410.fabricate;

import org.apache.commons.cli.*;
import org.m410.fabricate.global.GlobalRunner;
import org.m410.fabricate.project.ProjectRunner;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author m410
 */
public final class Main {
    static final String[] levels = {"debug", "info", "warn", "error"};

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Throwable {
        Options options = new Options();
        options.addOption("help", false, "Display help information");
        options.addOption("version", false, "Display version information");
        options.addOption("e", "env", true, "Set the environment name, defaults to 'default'");
        options.addOption("l", "log", true, "Set the output level, defaults to 'warn', options are [debug,info,warn,error]");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args);

        String logLevel = cmd.hasOption("l") ? cmd.getOptionValue("l").toLowerCase() : "warn";
        String env = cmd.hasOption("e") ? cmd.getOptionValue("e") : "default";

        if (!Arrays.asList(levels).contains(logLevel)) {
            System.out.println("Invalid logging level '" + logLevel + "', must be one of: " + Arrays.toString(levels));
            System.exit(1);
        }

        if(cmd.hasOption("version")) {
            System.out.println(ResourceBundle.getBundle("version").getString("version"));
        }
        else if(cmd.hasOption("help")) {
            new HelpFormatter().printHelp("fab", options, true);
            new GlobalRunner(cmd.getArgList()).outputCommands();
        }
        else if(cmd.getArgList().size() > 0 && isGlobal(cmd.getArgList().get(0))) {
            runGlobalCmd(cmd.getArgList());
        }
        else if(isProjectDir(System.getProperty("user.dir"))) {
            runProjectCmd(cmd.getArgList(), env, logLevel);
        }
        else {
            System.out.println("The current directory is not a project directory or unknown command.  Try 'fab -help'");
        }
    }

    protected static void runProjectCmd(List<String> argList, String envName, String logLevel) throws Throwable {
        new ProjectRunner(argList, envName, logLevel).run();
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
