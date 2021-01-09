package org.m410.fabricate.service.internal.serialize;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.config.Application;
import org.m410.fabricate.config.Build;
import org.m410.fabricate.config.Dependency;
import org.m410.fabricate.config.Module;

import java.util.List;
import java.util.Map;

/**
 * @author m410
 */
@Deprecated
public class CachedProject {
    private String hash;
    private Application application;
    private Build build;
    private List<Dependency> dependencies;
    private List<Module> modules;
    private Map<String,String> classpath;

    public CachedProject(BuildContext ctx) {
        this.hash = ctx.getHash();
        this.build = ctx.getBuild();
        this.dependencies = ctx.getDependencies();
        this.modules = ctx.getModules();
        this.classpath = ctx.getClasspath();
        this.application = ctx.getApplication();
    }

    public CachedProject() {
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Build getBuild() {
        return build;
    }

    public void setBuild(Build build) {
        this.build = build;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public Map<String, String> getClasspath() {
        return classpath;
    }

    public void setClasspath(Map<String, String> classpath) {
        this.classpath = classpath;
    }
}
