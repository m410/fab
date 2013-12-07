package org.m410.osgi.builder;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface Cli {
    String ask(String question);
    void warn(String in);
    void info(String in);
    void debug(String in);

}
