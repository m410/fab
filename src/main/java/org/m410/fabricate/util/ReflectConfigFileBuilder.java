package org.m410.fabricate.util;



import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Common way to create configuration files from the application.
 *
 * @author Michael Fortin
 */
public final  class ReflectConfigFileBuilder {
    private final String configClass = "org.apache.commons.configuration2.ImmutableHierarchicalConfiguration";
    private final String builderClassName;
    private final Path outputPath;
    private final Collection<File> classpath;
    private final boolean appCreate;
    private final String envName;
    private final String factoryClass;
    private final String appClassName;

    private ReflectConfigFileBuilder(String builderClassName, Path outputPath, Collection<File> classpath,
            boolean appCreate, String envName, String factoryClass, String appClassName) {
        this.builderClassName = builderClassName;
        this.outputPath = outputPath;
        this.classpath = classpath;
        this.appCreate = appCreate;
        this.envName = envName;
        this.factoryClass = factoryClass;
        this.appClassName = appClassName;
    }

    public ReflectConfigFileBuilder(String builderClassName) {
        outputPath = null;
        classpath = null;
        appCreate = false;
        envName = null;
        this.builderClassName = builderClassName;
        this.factoryClass = null;
        this.appClassName = null;
    }

    public ReflectConfigFileBuilder withPath(Path outputPath) {
        return new ReflectConfigFileBuilder(builderClassName, outputPath, classpath, appCreate, envName,
                factoryClass, appClassName);
    }

    public ReflectConfigFileBuilder withClasspath(Collection<File> paths) {
        return new ReflectConfigFileBuilder(builderClassName, outputPath, paths, appCreate, envName, factoryClass, appClassName);
    }

    public ReflectConfigFileBuilder withAppCreated(boolean withConfig) {
        return new ReflectConfigFileBuilder(builderClassName, outputPath, classpath, withConfig, envName, factoryClass, appClassName);
    }

    public ReflectConfigFileBuilder withEnv(String envName) {
        return new ReflectConfigFileBuilder(builderClassName, outputPath, classpath, appCreate, envName, factoryClass, appClassName);
    }

    public ReflectConfigFileBuilder withFactoryClass(String factoryClass) {
        return new ReflectConfigFileBuilder(builderClassName, outputPath, classpath, appCreate, envName,
                factoryClass, appClassName);
    }

    public ReflectConfigFileBuilder withAppClass(String appClassName) {
        return new ReflectConfigFileBuilder(builderClassName, outputPath, classpath, appCreate, envName, factoryClass, appClassName);
    }

    @SuppressWarnings("unchecked")
    public void make() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException,
            IOException {

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URLClassLoader loader = new URLClassLoader(getUrls(), contextClassLoader);
        Class fileBuilderClazz = loader.loadClass(builderClassName);

        Class confFactoryClazz = loader.loadClass(factoryClass);
        Method buildtimeMethod = confFactoryClazz.getMethod("buildtime", String.class);
        Class confClazz = loader.loadClass(configClass);

        // instance of ImmutableHierarchicalConfiguration
        Object configInstance = buildtimeMethod.invoke(null, envName);

        Object builderInstance;

        if (appCreate) {
            builderInstance = appCreate(loader);
        }
        else
            builderInstance = fileBuilderClazz.newInstance();

        Method writeToFile = fileBuilderClazz.getMethod("writeToFile", Path.class, confClazz);

        outputPath.toFile().getParentFile().mkdirs();

        System.out.println("writeToFile:" + writeToFile);
        System.out.println("builderInstance:" + builderInstance);

        writeToFile.invoke(builderInstance, outputPath, configInstance);
    }

    // todo this is also app specific, should be removed
    private Object appCreate(ClassLoader loader)
            throws IOException, ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {

        Class appClazz = loader.loadClass(appClassName);
        Object applicationInstance = appClazz.newInstance();

        Method configureBuilderMethod = appClazz.getMethod("configureBuilder");
        System.out.println("configBuilderMethod:" + configureBuilderMethod);
        return configureBuilderMethod.invoke(applicationInstance);
    }



    private URL[] getUrls() throws MalformedURLException {
        Path classes = FileSystems.getDefault().getPath("target/classes");
        List<URL> classPath = new ArrayList<>();
        classPath.add(classes.toUri().toURL());

        for (File file : classpath) {
            classPath.add(file.toURI().toURL());
        }

        return classPath.toArray(new URL[classPath.size()]);
    }
}
