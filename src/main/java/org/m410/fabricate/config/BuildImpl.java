package org.m410.fabricate.config;

import org.apache.commons.configuration2.ImmutableConfiguration;

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
    private String cacheDir;

    public BuildImpl() {
    }

    public BuildImpl(ImmutableConfiguration config) {
        defaultEnvironment = config.getString("build.defaultEnvironment", "development");
        defaultCommand = config.getString("build.defaultCommand", "build");
        lang = config.getString("build.lang", "java");
        langVersion = config.getString("build.langVersion", "1.8");
        compilerArgs = config.getString("build.compilerArgs", "-ea");
        targetDir = config.getString("build.targetDir", "target");
        webappDir = config.getString("build.webappDir", "webapp");
        sourceDir = config.getString("build.sourceDir", "src/java");
        sourceOutputDir = config.getString("build.sourceOutputDir", "dist/classes");
        resourceDir = config.getString("build.resourceDir", "src/resources");
        testDir = config.getString("build.testDir", "test/java");
        testOutputDir = config.getString("testOutputDir","target/test-classes");
        testResourceDir = config.getString("testResourceDir","test/resources");
        vcs = config.getString("vcs","git");
        packageSource = config.getBoolean("build.packageSource", false);
        packageDocs = config.getBoolean("build.packageDocs", false);
        packageClassifier = config.getString("build.packageClassifier");
        packageName = config.getString("build.packageName");
        defaultLogLevel = config.getString("defaultLogLevel","info");
        cacheDir = config.getString("build.cacheDir", ".fab");
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
    public String getCacheDir() {
        return cacheDir;
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

    public void setDefaultEnvironment(String defaultEnvironment) {
        this.defaultEnvironment = defaultEnvironment;
    }

    public void setDefaultCommand(String defaultCommand) {
        this.defaultCommand = defaultCommand;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setLangVersion(String langVersion) {
        this.langVersion = langVersion;
    }

    public void setCompilerArgs(String compilerArgs) {
        this.compilerArgs = compilerArgs;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public void setWebappDir(String webappDir) {
        this.webappDir = webappDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public void setSourceOutputDir(String sourceOutputDir) {
        this.sourceOutputDir = sourceOutputDir;
    }

    public void setResourceDir(String resourceDir) {
        this.resourceDir = resourceDir;
    }

    public void setTestDir(String testDir) {
        this.testDir = testDir;
    }

    public void setTestOutputDir(String testOutputDir) {
        this.testOutputDir = testOutputDir;
    }

    public void setTestResourceDir(String testResourceDir) {
        this.testResourceDir = testResourceDir;
    }

    public void setVcs(String vcs) {
        this.vcs = vcs;
    }

    public void setPackageSource(boolean packageSource) {
        this.packageSource = packageSource;
    }

    public void setPackageDocs(boolean packageDocs) {
        this.packageDocs = packageDocs;
    }

    public void setPackageClassifier(String packageClassifier) {
        this.packageClassifier = packageClassifier;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setDefaultLogLevel(String defaultLogLevel) {
        this.defaultLogLevel = defaultLogLevel;
    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    @Override
    public String toString() {
        return "BuildImpl{" +
                "defaultEnvironment='" + defaultEnvironment + '\'' +
                ", defaultCommand='" + defaultCommand + '\'' +
                ", lang='" + lang + '\'' +
                ", langVersion='" + langVersion + '\'' +
                ", compilerArgs='" + compilerArgs + '\'' +
                ", targetDir='" + targetDir + '\'' +
                ", webappDir='" + webappDir + '\'' +
                ", sourceDir='" + sourceDir + '\'' +
                ", sourceOutputDir='" + sourceOutputDir + '\'' +
                ", resourceDir='" + resourceDir + '\'' +
                ", testDir='" + testDir + '\'' +
                ", testOutputDir='" + testOutputDir + '\'' +
                ", testResourceDir='" + testResourceDir + '\'' +
                ", vcs='" + vcs + '\'' +
                ", packageSource=" + packageSource +
                ", packageDocs=" + packageDocs +
                ", packageClassifier='" + packageClassifier + '\'' +
                ", packageName='" + packageName + '\'' +
                ", defaultLogLevel='" + defaultLogLevel + '\'' +
                '}';
    }
}
