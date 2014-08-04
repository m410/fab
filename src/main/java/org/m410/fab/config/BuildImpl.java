package org.m410.fab.config;

import java.util.Map;

/**
 * @author m410
 */
public class BuildImpl implements Build {

    private String defaultEnvironment;
    private String defaultCommand;
    private String lang;
    private String langVersion;
    private String compilerArgs;
    private String targetDir;
    private String webappDir;
    private String sourceDir;
    private String sourceOutputDir;
    private String resourceDir;
    private String testDir;
    private String testOutputDir;
    private String testResourceDir;
    private String vcs;
    private boolean packageSource;
    private boolean packageDocs;
    private String packageClassifier;
    private String packageName;
    private String defaultLogLevel;

    public BuildImpl(Map<String, Object> data) {

        if(data == null)
            return;

        defaultEnvironment = (String)data.get("defaultEnvironment");
        defaultCommand = (String)data.get("defaultCommand");
        lang = (String)data.get("lang");
        langVersion = (String)data.get("langVersion");
        compilerArgs = (String)data.get("compilerArgs");
        targetDir = (String)data.get("targetDir");
        webappDir = (String)data.get("webappDir");
        sourceDir = (String)data.get("sourceDir");
        sourceOutputDir = (String)data.get("sourceOutputDir");
        resourceDir = (String)data.get("resourceDir");
        testDir = (String)data.get("testDir");
        testOutputDir = (String)data.get("testOutputDir");
        testResourceDir = (String)data.get("testResourceDir");
        vcs = (String)data.get("vcs");
        packageSource = (boolean)data.getOrDefault(" packageSource",false);
        packageDocs = (boolean)data.getOrDefault(" packageDocs",false);
        packageClassifier = (String)data.get("packageClassifier");
        packageName = (String)data.get("packageName");
        defaultLogLevel = (String)data.get("defaultLogLevel");
    }

    @Override
    public String getDefaultEnvironment() {
        return defaultEnvironment;
    }

    @Override
    public String getDefaultCommand() {
        return defaultCommand;
    }

    @Override
    public String getLang() {
        return lang;
    }

    @Override
    public String getLangVersion() {
        return langVersion;
    }

    @Override
    public String getCompilerArgs() {
        return compilerArgs;
    }

    @Override
    public String getTargetDir() {
        return targetDir;
    }

    @Override
    public String getWebappDir() {
        return webappDir;
    }

    @Override
    public String getSourceDir() {
        return sourceDir;
    }

    @Override
    public String getSourceOutputDir() {
        return sourceOutputDir;
    }

    @Override
    public String getResourceDir() {
        return resourceDir;
    }

    @Override
    public String getTestDir() {
        return testDir;
    }

    @Override
    public String getTestOutputDir() {
        return testOutputDir;
    }

    @Override
    public String getTestResourceDir() {
        return testResourceDir;
    }

    @Override
    public String getVcs() {
        return vcs;
    }

    @Override
    public boolean isPackageSource() {
        return packageSource;
    }

    @Override
    public boolean isPackageDocs() {
        return packageDocs;
    }

    @Override
    public String getPackageClassifier() {
        return packageClassifier;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getDefaultLogLevel() {
        return defaultLogLevel;
    }


}
