package org.m410.fab.garden.support;

/**
 * @author m410
 */
public final class ReloadingEvent {
    private ClassLoader classLoader;
    private Object application;
    private boolean release = false;

    public ReloadingEvent(boolean release) {
        this.release = release;
    }

    public ReloadingEvent(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ReloadingEvent(ClassLoader classLoader, Object application) {
        this.classLoader = classLoader;
        this.application = application;
    }

    public Object getApplication() {
        return application;
    }

    public boolean isRelease() {
        return release;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
