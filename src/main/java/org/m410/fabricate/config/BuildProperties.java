package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

/**
 * Build properties for a build run.
 *
 * @author m410
 */
@Deprecated
public class BuildProperties {
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
    private String packageSource;
    private String packageDocs;
    private String packageClassifier;
    private String packageName;

    public BuildProperties(ImmutableHierarchicalConfiguration base) {
        // todo implement me..
    }

    public String getDefaultEnvironment() {
        return defaultEnvironment;
    }

    public void setDefaultEnvironment(String defaultEnvironment) {
        this.defaultEnvironment = defaultEnvironment;
    }

    public String getDefaultCommand() {
        return defaultCommand;
    }

    public void setDefaultCommand(String defaultCommand) {
        this.defaultCommand = defaultCommand;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLangVersion() {
        return langVersion;
    }

    public void setLangVersion(String langVersion) {
        this.langVersion = langVersion;
    }

    public String getCompilerArgs() {
        return compilerArgs;
    }

    public void setCompilerArgs(String compilerArgs) {
        this.compilerArgs = compilerArgs;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public String getWebappDir() {
        return webappDir;
    }

    public void setWebappDir(String webappDir) {
        this.webappDir = webappDir;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public String getSourceOutputDir() {
        return sourceOutputDir;
    }

    public void setSourceOutputDir(String sourceOutputDir) {
        this.sourceOutputDir = sourceOutputDir;
    }

    public String getResourceDir() {
        return resourceDir;
    }

    public void setResourceDir(String resourceDir) {
        this.resourceDir = resourceDir;
    }

    public String getTestDir() {
        return testDir;
    }

    public void setTestDir(String testDir) {
        this.testDir = testDir;
    }

    public String getTestOutputDir() {
        return testOutputDir;
    }

    public void setTestOutputDir(String testOutputDir) {
        this.testOutputDir = testOutputDir;
    }

    public String getTestResourceDir() {
        return testResourceDir;
    }

    public void setTestResourceDir(String testResourceDir) {
        this.testResourceDir = testResourceDir;
    }

    public String getVcs() {
        return vcs;
    }

    public void setVcs(String vcs) {
        this.vcs = vcs;
    }

    public String getPackageSource() {
        return packageSource;
    }

    public void setPackageSource(String packageSource) {
        this.packageSource = packageSource;
    }

    public String getPackageDocs() {
        return packageDocs;
    }

    public void setPackageDocs(String packageDocs) {
        this.packageDocs = packageDocs;
    }

    public String getPackageClassifier() {
        return packageClassifier;
    }

    public void setPackageClassifier(String packageClassifier) {
        this.packageClassifier = packageClassifier;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private static boolean empty(String s) {
        return s == null || "".equals(s.trim());
    }
}
