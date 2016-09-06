package org.m410.fabricate.config;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Michael Fortin
 */
public final class Resolver {

    static final Repository defaultRepo = new Repository("http://repo.m410.org/content/repositories/releases/");

    /**
     * @param reference    the project reference to resolve to a remote reference.
     * @param repositories placeholder arg, should list of remote repositories
     *                     to resolve where to download the reference if the base_config flag doesn't
     *                     exist.
     * @return a new reference to the remote file, or thows exception if it can't find it.
     */
    public static Reference resolveRemote(Reference reference, File cacheDir, List<Repository> repositories) {
        final File localCacheFile = cacheConfigFile(reference, cacheDir);

        if (localCacheFile.exists()) {
            return new RemoteReference(reference, localCacheFile);
        }
        else if (reference.getRemoteReference().isPresent()) {
            writeToFile(reference.getRemoteReference().get(), localCacheFile);
            return new RemoteReference(reference, localCacheFile);
        }
        else {
            // todo test each repository
            URL url = makeUrl(reference, repositories);
            writeToFile(url, localCacheFile);
            return new RemoteReference(reference, localCacheFile);
        }
    }

    static void writeToFile(URL inputUrl, File outputFile) {
        try (BufferedInputStream input = new BufferedInputStream(inputUrl.openStream());
             BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            copyStream(input, output);
        }
        catch (IOException e) {
            throw new RuntimeException("could not copy to local disk", e);
        }
    }

    static URL makeUrl(Reference resource, List<Repository> repositories) {

        try {
            return new URL(repositories.get(0).getRoot() + // todo enable for many repositories
                           resource.getOrg().replaceAll("\\.", "/") + "/" +
                           resource.getName() + "/" +
                           resource.getVersion() + "/" +
                           resource.getName() + "-" + resource.getVersion() + ".yml");
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private static File cacheConfigFile(Reference resource, File cacheDir) {
        Path path = FileSystems.getDefault().getPath(
                cacheDir.getAbsolutePath(),
                resource.getOrg().replaceAll("\\.", "/"),
                resource.getName(),
                resource.getVersion());

        path.toFile().mkdirs();

        return FileSystems.getDefault().getPath(
                cacheDir.getAbsolutePath(),
                resource.getOrg().replaceAll("\\.", "/"),
                resource.getName(),
                resource.getVersion(),
                "configuration.yml")
                .toFile();
    }
}
