package org.m410.fab.builder;

import org.m410.fab.config.Application;
import org.m410.fab.config.Build;
import org.m410.fab.config.Dependency;
import org.m410.fab.config.Module;

import java.util.List;
import java.util.Map;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface BuildContext {
    String getHash();
    boolean isCachedConfig();
    String environment();
    Cli cli();

    // how to handle caching of environments, maybe separate files?
    // cached elements
    Application getApplication();
    Build getBuild();
    List<Dependency> getDependencies();
    List<? extends Module> getModules();
    Map<String,String> getClasspath();
}
