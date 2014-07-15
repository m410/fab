package org.m410.fab;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

import java.util.List;

/**
 * @author m410
 */
public final class Main {

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("h", false, "Display help information");
        options.addOption("v", false, "Display version information");
        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse( options, args);

        if(isGlobal(cmd.getArgList().get(0).toString()))
            runGlobalCmd(cmd.getArgList());
        else if(isProjectDir(System.getProperty("user.dir")))
            runProjectCmd(cmd.getArgList());
    }

    private static void runProjectCmd(List argList) {

    }

    private static void runGlobalCmd(List argList) {

    }

    private static boolean isProjectDir(String property) {
        return false;
    }

    private static boolean isGlobal(String s) {
        return false;
    }
}
