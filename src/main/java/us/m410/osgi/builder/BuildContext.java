package us.m410.osgi.builder;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface BuildContext {
    Application getApplication();
    Build getBuild();

    Cli cli();
}
