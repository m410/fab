package org.m410.fabricate.config;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * @author m410
 */
public abstract class Base {
    protected URL url;
    protected Map<String, Object> configuration;

    static final String ORGANIZATION = "organization";
    static final String NAME = "name";
    static final String VERSION = "version";

    List<BundleRef> getBundles() throws MalformedURLException {
        return null;
    }

    public URL makeUrl(Map<String, String> resource, File cacheDir, List<String> repositories)
            throws IOException {

        // todo enable for many repositories

        URL url = resource.get("base_config") != null ?
                new URL(resource.get("base_config")) :
                new URL("http://repo.m410.org/content/repositories/releases/" +
                        getOrThrow(resource, ORGANIZATION).replaceAll("\\.", "/") + "/" +
                        getOrThrow(resource, NAME) + "/" +
                        getOrThrow(resource, VERSION) +"/"+
                        getOrThrow(resource, NAME) +"-"+getOrThrow(resource, VERSION)+".yml");

        final File file = cacheConfigFile(resource, cacheDir);

        if(!file.exists()) {
            final BufferedInputStream input = new BufferedInputStream(url.openStream());
            final BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
            copyStream(input, output);
            input.close();
            output.close();
        }

        return file.toURI().toURL();
    }

    private static final String getOrThrow(Map<String, String> resource, String key) {
        String result = resource.get(key);

        if(result == null)
            throw new InvalidConfigurationException("Missing module property: " + key + " in " + resource);

        return result;
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    File cacheConfigFile(Map<String, String> resource, File cacheDir) {
        Path path = FileSystems.getDefault().getPath(
                cacheDir.getAbsolutePath(),
                getOrThrow(resource, ORGANIZATION).replaceAll("\\.", "/"),
                getOrThrow(resource, NAME),
                getOrThrow(resource, VERSION)
        );

        path.toFile().mkdirs();

        path = FileSystems.getDefault().getPath(
                cacheDir.getAbsolutePath(),
                getOrThrow(resource, ORGANIZATION).replaceAll("\\.", "/"),
                getOrThrow(resource, NAME),
                getOrThrow(resource, VERSION),
                "configuration.yml"
        );

        return path.toFile();
    }
}
