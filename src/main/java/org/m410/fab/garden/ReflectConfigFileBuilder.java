package org.m410.fab.garden;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Common way to create configuration files from the application.
 *
 * @author Michael Fortin
 */
public final  class ReflectConfigFileBuilder {
    private final String builderClassName;
    private final Path outputPath;
    private final Collection<File> classpath;
    private final boolean appCreate;
    private final String envName;

    private ReflectConfigFileBuilder(String builderClassName, Path outputPath, Collection<File> classpath,
            boolean appCreate, String envName) {
        this.builderClassName = builderClassName;
        this.outputPath = outputPath;
        this.classpath = classpath;
        this.appCreate = appCreate;
        this.envName = envName;
    }

    public ReflectConfigFileBuilder(String builderClassName) {
        outputPath = null;
        classpath = null;
        appCreate = false;
        envName = null;
        this.builderClassName = builderClassName;
    }

    public ReflectConfigFileBuilder withPath(Path outputPath) {
        return new ReflectConfigFileBuilder(builderClassName,outputPath, classpath, appCreate, envName);
    }

    public ReflectConfigFileBuilder withClasspath(Collection<File> paths) {
        return new ReflectConfigFileBuilder(builderClassName,outputPath,paths, appCreate, envName);
    }

    public ReflectConfigFileBuilder withAppCreated(boolean withConfig) {
        return new ReflectConfigFileBuilder(builderClassName,outputPath, classpath, withConfig, envName);
    }

    public ReflectConfigFileBuilder withEnv(String name) {
        return new ReflectConfigFileBuilder(builderClassName,outputPath, classpath, appCreate, name);
    }

    @SuppressWarnings("unchecked")
    public void make() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException,
            IOException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URLClassLoader loader = new URLClassLoader(getUrls(), contextClassLoader);
        Class fileBuilderClazz = loader.loadClass(builderClassName);
        Class confClazz = loader.loadClass("org.m410.garden.configuration.Configuration");
        Class confFactoryClazz = loader.loadClass("org.m410.garden.configuration.ConfigurationFactory");
        Method buildtimeMethod = confFactoryClazz.getMethod("buildtime", String.class);
        Object configurationInstance = buildtimeMethod.invoke(null,envName);

        Object builderInstance;

        if(appCreate)
            builderInstance = appCreate(loader, confClazz, configurationInstance);
        else
            builderInstance = fileBuilderClazz.newInstance();

        Method writeToFile = fileBuilderClazz.getMethod("writeToFile", Path.class, confClazz);

        outputPath.toFile().getParentFile().mkdirs();
        writeToFile.invoke(builderInstance, outputPath, configurationInstance);
    }

    private Object appCreate(ClassLoader loader, Class confClazz, Object configurationInstance)
            throws IOException, ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class appClazz = loader.loadClass(appClassName());
        Object applicationInstance = appClazz.newInstance();

        Method configureBuilderMethod = appClazz.getMethod("configureBuilder", confClazz);
        return configureBuilderMethod.invoke(applicationInstance, configurationInstance);
    }

    private String appClassName() throws IOException {
        // todo fix hardcode
        Path configurationFile = FileSystems.getDefault().getPath("garden.fab.yml");
        List<String> lines = Files.readAllLines(configurationFile, StandardCharsets.UTF_8);
        Optional<String> line = Optional.empty();

        for (String s : lines)
            if(s.contains("applicationClass"))
                line = Optional.of(s.substring(s.indexOf(":")+1).trim());

        String className = null;

        if (line.isPresent())
            className = line.get();
        else
            throw new RuntimeException("could not find applicationClass line 1");

        return className;
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
