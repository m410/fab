package org.m410.fab.project;

import java.net.MalformedURLException;
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
    private Archetype archetype;
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

    public Archetype getArchetype() {
        return archetype;
    }

    public void setArchetype(Archetype archetype) {
        this.archetype = archetype;
    }

    public URL makeUrl() throws MalformedURLException {
        if(archetype.getBase() != null)
            return archetype.getBase();
        else
            return new URL(new StringBuilder()
                    .append("http://repo.m410.org/content/repositories/snapshots/")
                    .append(archetype.getOrganization().replaceAll("\\.", "/"))
                    .append("/")
                    .append(archetype.getName())
                    .append("/")
                    .append(archetype.getVersion())
                    .append(".yml")
                    .toString());
    }

    public Set<String> verifyResources() {
        return new HashSet<>();
    }

    public List<BundleRef> resources() {
        List<BundleRef> list = new ArrayList<>();

        if(baseConfig != null && baseConfig.getBundles() != null)
            list.addAll(baseConfig.getBundles());
        if(bundles != null)
            list.addAll(bundles);
        if(modules != null)
            list.addAll(modules);
        if(persistence != null)
            list.addAll(persistence);
        if(view != null)
            list.addAll(view);

        return list;
    }

    @Override
    public String toString() {
        return "BuildConfig{" +
                "archetype=" + archetype +
                ", baseConfig=" + baseConfig +
                ", bundles=" + bundles +
                ", modules=" + modules +
                ", persistence=" + persistence +
                ", view=" + view +
                '}';
    }
}
