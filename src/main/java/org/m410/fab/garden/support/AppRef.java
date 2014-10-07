package org.m410.fab.garden.support;

/**
 * @author m410
 */
public final class AppRef {
    private final Object application;
    private final ClassLoader classLoader;
    private final Class appClass;

    public AppRef(Object application, Class appClass, ClassLoader classLoader) {
        this.application = application;
        this.classLoader = classLoader;
        this.appClass = appClass;
    }

    public Class getAppClass() {
        return appClass;
    }

    public Object getApplication() {
        return application;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
