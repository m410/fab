package org.m410.fab.config;

import java.io.Serializable;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface Build extends Serializable {

    String getDefaultLogLevel();
    String getDefaultEnvironment();
    String getDefaultCommand();
    String getLang();
    String getLangVersion();
    String getCompilerArgs();
    String getTargetDir();
    String getWebappDir();
    String getSourceDir();// mainSourceDir
    String getSourceOutputDir();// mainSourceOutput
    String getResourceDir();// mainResourcesDir
    String getTestDir(); //testSourceDir
    String getTestOutputDir();
    String getTestResourceDir();
    String getVcs();
    String getCacheDir();

    boolean isPackageSource();
    boolean isPackageDocs();
    String getPackageClassifier();
    String getPackageName();

}
