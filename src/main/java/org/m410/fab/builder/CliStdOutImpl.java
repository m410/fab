package org.m410.fab.builder;

/**
 * @author m410
 */
public class CliStdOutImpl implements Cli {

    private String logLevel = "debug";

    public CliStdOutImpl(String logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public String ask(String question) {
        return null;
    }

    @Override
    public void warn(String in) {
        System.out.println("WARN: " + in);
    }

    @Override
    public void info(String in) {
        System.out.println("INFO: " + in);
    }

    @Override
    public void debug(String in) {
        System.out.println("DEBUG:" + in);
    }

    @Override
    public void error(String in) {
        System.out.println("ERROR:" + in);
    }
}
