package org.m410.fab.builder;

/**
 * @author m410
 */
public class BuildContextImpl implements BuildContext {
    private Cli cli = new CliStdOutImpl();

    @Override
    public Application getApplication() {
        return null;
    }

    @Override
    public Build getBuild() {
        return null;
    }

    @Override
    public Cli cli() {
        return cli;
    }
}
