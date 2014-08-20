package org.m410.fab.garden.web;

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

    static URL[] toURLs(List<URL> runPath, File classesDir) {
        List<URL> urls = new ArrayList<>();

        try {
            urls.add(classesDir.toURI().toURL());
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        urls.addAll(runPath);

//        log.debug("CLASSPATH: {}", urls);
//        for (URL url : urls) {
//            System.out.println("    " + url);
//        }

        return urls.toArray(new URL[urls.size()]);
    }

    public LocalDevClassLoader(List<URL> runPath, File classesDir, ClassLoader parent) {
        super(toURLs(runPath, classesDir), parent);
//        dumpThreadClasspath();
    }


//
//    void dumpThreadClasspath() {
//        ClassLoader classloader = this;
//
//        do {
//            URL[] urls = ((URLClassLoader)classloader).getURLs();
//
//            for(URL url: urls){
//                System.out.println("    " + url.getFile());
//            }
//            classloader = classloader.getParent();
//
//        } while(classloader != null);
//    }
}
