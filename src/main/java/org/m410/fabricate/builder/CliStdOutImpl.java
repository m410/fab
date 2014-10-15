package org.m410.fabricate.builder;

/**
 * @author m410
 */
public class CliStdOutImpl implements Cli {
    private static final String[] levels = {"debug","info","warn","error"};

    private int level = 1;

    public CliStdOutImpl(final String logLevel) {
        switch(logLevel.toLowerCase()) {
            case "debug":
                level = 0;
                break;
            case "info":
                level = 1;
                break;
            case "warn":
                level = 2;
                break;
            case "error":
                level = 3;
                break;
            default:
                level = 1;
        }
    }

    @Override
    public String ask(String question) {
        return null;
    }

    @Override
    public void warn(String in) {
        if(level <= 2 )
            System.out.println("  WARN|" + in);
    }

    @Override
    public void info(String in) {
        if(level <= 1 )
            System.out.println("  INFO |" + in);
    }

    @Override
    public void debug(String in) {
        if(level <= 0 )
            System.out.println("  DEBUG|" + in);
    }

    @Override
    public void error(String in) {
        if(level <= 3 )
            System.err.println("  ERROR|" + in);
    }

    @Override
    public void println(String s) {
        System.out.println("  " + s);
    }
}
