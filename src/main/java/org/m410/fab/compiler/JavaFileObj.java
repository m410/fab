package org.m410.fab.compiler;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * @author m410
 */
public class JavaFileObj extends SimpleJavaFileObject {
    public JavaFileObj(URI uri, Kind kind) {
        super(uri, kind);
    }
}
