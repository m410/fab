package org.m410.fab.garden.support;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public class LocalDevClassLoader extends URLClassLoader {

    public LocalDevClassLoader(List<URL> runPath, File classesDir, ClassLoader parent) {
        super(toURLs(runPath, classesDir), parent);
//        dumpThreadClasspath();
    }

    static URL[] toURLs(List<URL> runPath, File classesDir) {
        List<URL> urls = new ArrayList<>();

        try {
            urls.add(classesDir.toURI().toURL());
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        if(runPath != null)
            urls.addAll(runPath);

//        System.out.println("CLASSPATH: " + urls);
        for (URL url : urls)
            System.out.println("    " + url);

        return urls.toArray(new URL[urls.size()]);
    }



    void dumpThreadClasspath() {
        ClassLoader classloader = this;

        do {
            URL[] urls = ((URLClassLoader)classloader).getURLs();

            for(URL url: urls){
                System.out.println("    " + url.getFile());
            }
            classloader = classloader.getParent();

        } while(classloader != null);
    }
}
