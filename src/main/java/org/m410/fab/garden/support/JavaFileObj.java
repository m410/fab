package org.m410.fab.garden.support;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author m410
 */
public final class JavaFileObj extends SimpleJavaFileObject {
    public JavaFileObj(URI uri, Kind kind) {
        super(uri, kind);
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return new String(Files.readAllBytes(Paths.get(this.uri)), "UTF-8");
    }
}
