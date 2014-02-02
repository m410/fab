package org.m410.fab.builder;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface BuildContext {
    Application getApplication();
    Build getBuild();

    // environment
    // dependencies
    // paths

    Cli cli();
}
