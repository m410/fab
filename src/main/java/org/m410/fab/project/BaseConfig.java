package org.m410.fab.project;

import org.m410.fab.config.BuildProperties;

import java.util.List;
import java.util.Map;

/**
 * A reference to base bundles and configuration
 *
 * @author m410
 */
@Deprecated
public class BaseConfig {
    private List<BundledRef> bundles;
    private BuildProperties build;
    private String source;
    private Map<String, Object> fullConfig;

    public List<BundledRef> getBundles() {
        return bundles;
    }

    public void setBundles(List<BundledRef> bundles) {
        this.bundles = bundles;
    }

    public BuildProperties getBuild() {
        return build;
    }

    public void setBuild(BuildProperties build) {
        this.build = build;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "BaseConfig{" +
                "bundles=" + bundles +
                ", build=" + build +
                ", source='" + source + '\'' +
                '}';
    }

    public void setFullConfig(Map<String, Object> fullConfig) {
        this.fullConfig = fullConfig;
    }

    public Map<String, Object> getFullConfig() {
        return fullConfig;
    }
}
