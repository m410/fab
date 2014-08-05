package org.m410.fab.project;

import java.util.List;

/**
 * A reference to base bundles and configuration
 *
 * @author m410
 */
public class BaseConfig {
    private List<BundleRef> bundles;
    private BuildProperties build;

    public List<BundleRef> getBundles() {
        return bundles;
    }

    public void setBundles(List<BundleRef> bundles) {
        this.bundles = bundles;
    }

    public BuildProperties getBuild() {
        return build;
    }

    public void setBuild(BuildProperties build) {
        this.build = build;
    }
}
