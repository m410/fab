package org.m410.fabricate.builder;

import org.m410.fabricate.config.Application;
import org.m410.fabricate.config.Build;
import org.m410.fabricate.config.Dependency;
import org.m410.fabricate.config.Module;

import java.util.List;
import java.util.Map;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface BuildContext {
    String getHash();
    boolean isFromCache();
    String environment();
    Cli cli();

    // how to handle caching of environments, maybe separate files?
    // cached elements
    Application getApplication();
    Build getBuild();
    List<Dependency> getDependencies();
    List<Module> getModules();
    Map<String,String> getClasspath();
}
