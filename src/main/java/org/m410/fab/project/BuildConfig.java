package org.m410.fab.project;

import java.net.URL;
import java.util.*;

/**
 * A projects configuration.  All projects at a minimum must define it's name version and
 * organization, it's base configuration, it's environments, and any modules.  Just enough
 * information to download necessary resources and bootstrap the build.
 *
 * @author m410
 */
public class BuildConfig {
    private URL url;

    private String name;
    private String build_version;
    private String build_org;
    private BaseConfig baseConfig;

    private List<BundleRef> bundles;
    private List<BundleRef> modules;
    private List<BundleRef> persistence;
    private List<BundleRef> view;

    public BaseConfig getBaseConfig() {
        return baseConfig;
    }

    public void setBaseConfig(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    public List<BundleRef> getBundles() {
        return bundles;
    }

    public void setBundles(List<BundleRef> bundles) {
        this.bundles = bundles;
    }

    public List<BundleRef> getModules() {
        return modules;
    }

    public void setModules(List<BundleRef> modules) {
        this.modules = modules;
    }

    public List<BundleRef> getPersistence() {
        return persistence;
    }

    public void setPersistence(List<BundleRef> persistence) {
        this.persistence = persistence;
    }

    public List<BundleRef> getView() {
        return view;
    }

    public void setView(List<BundleRef> view) {
        this.view = view;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuild_version() {
        return build_version;
    }

    public void setBuild_version(String build_version) {
        this.build_version = build_version;
    }

    public String getBuild_org() {
        return build_org;
    }

    public void setBuild_org(String build_org) {
        this.build_org = build_org;
    }

    public URL makeUrl() {
        // todo need to test multiple urls
        return url;
    }

    public Set<String> verifyResources() {
        return new HashSet<>();
    }

    public List<BundleRef> resources() {
        List<BundleRef> list = new ArrayList<>();
        list.addAll(baseConfig.getBundles());
        list.addAll(bundles);
        list.addAll(modules);
        list.addAll(persistence);
        list.addAll(view);
        return list;
    }

    @Override
    public String toString() {
        return "BuildConfig{" +
                "url=" + url +
                ", name='" + name + '\'' +
                ", build_version='" + build_version + '\'' +
                ", build_org='" + build_org + '\'' +
                ", baseConfig=" + baseConfig +
                ", bundles=" + bundles +
                ", modules=" + modules +
                ", persistence=" + persistence +
                ", view=" + view +
                '}';
    }
}
