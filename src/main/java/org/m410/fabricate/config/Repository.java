package org.m410.fabricate.config;

/**
 * @author Michael Fortin
 */
public final class Repository {
    private final String root;

    public Repository(String root) {
        this.root = root;
    }

    public String getRoot() {
        return root;
    }
}
