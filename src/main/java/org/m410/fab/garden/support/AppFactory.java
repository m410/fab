package org.m410.fab.garden.support;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

/**
 * @author m410
 */
public final class AppFactory {
    List<URL> runPath;
    File classesDir;
    String loaderClassName;
    String applicationClassName;
    String envName;

    public AppFactory(List<URL> runPath, File classesDir, String applicationClassName, String loaderClassName, String envName) {
        this.runPath = runPath;
        this.classesDir = classesDir;
        this.loaderClassName = loaderClassName;
        this.applicationClassName = applicationClassName;
        this.envName = envName;
    }

    public AppRef make() throws IllegalAccessException, InstantiationException, NoSuchMethodException,
            ClassNotFoundException, InvocationTargetException {
        final ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();// url classloader
        ClassLoader classLoader = new LocalDevClassLoader(runPath, classesDir, threadClassLoader);

        Class appLoaderClass = classLoader.loadClass(loaderClassName);
        Object appLoader = appLoaderClass.newInstance();
        Method loaderMethod = appLoaderClass.getMethod("load", String.class);
        Object application = loaderMethod.invoke(appLoader, envName);
        Class appClass = classLoader.loadClass(applicationClassName);

        return new AppRef(application, appClass, classLoader);
    }
}
