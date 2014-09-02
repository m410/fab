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
    void setHash(String hash);
    Application getApplication();
    Build getBuild();
    String environment();
    List<Dependency> getDependencies();
    List<? extends Module> getModules();
    Cli cli();
    Map<String,String> getClasspath();
}
