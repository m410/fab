package org.m410.fabricate.builder;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.m410.fabricate.config.Application;
import org.m410.fabricate.config.Build;
import org.m410.fabricate.config.Dependency;
import org.m410.fabricate.config.Module;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    Application getApplication();

    Build getBuild();

    List<Dependency> getDependencies();

    List<Module> getModules();

    Map<String,String> getClasspath();

    ImmutableHierarchicalConfiguration getConfiguration();

    Optional<ImmutableHierarchicalConfiguration> configAt(String org, String name);
}
