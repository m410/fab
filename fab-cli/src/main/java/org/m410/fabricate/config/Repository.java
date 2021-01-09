package org.m410.fabricate.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Michael Fortin
 */
public final class Repository {
    private final String url;
    private final String id;

    public Repository(String id, String url) {
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("url", url)
                .append("id", id)
                .toString();
    }
}
