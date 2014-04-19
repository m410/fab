package org.m410.fab.builder;

import org.m410.fab.config.Application;
import org.m410.fab.config.Build;
import org.m410.fab.config.Dependency;
import org.m410.fab.config.Module;

import java.util.List;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface BuildContext {
    Application application();
    Build build();
    String environment();
    List<Dependency> dependencies();
    List<? extends Module> modules();
    Cli cli();
}
