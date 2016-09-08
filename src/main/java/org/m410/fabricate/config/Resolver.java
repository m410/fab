package org.m410.fabricate.config;


import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Resolves references to external configuration files and bundles.  This should be an
 * instance class with it's configuration setup when initialized instead of a static
 * helper. It needs a list of repositories and cache directory location at the very least.
 *
 * @author Michael Fortin
 */
public final class Resolver {

    static final Repository defaultRepo = new Repository("m410","http://repo.m410.org/content/repositories/releases/");
    static final Repository snapshotRepo = new Repository("m410-snapshot","http://repo.m410.org/content/repositories/snapshots/");
    static final Pattern snapshotVersionPattern = Pattern.compile("(?smi)<extension>jar</extension>.*?<value>(.*?)</value>");

    private File cacheDir;
    private List<Repository> repositories;

    /**
     * @param cacheDirectory
     * @param repositories placeholder arg, should list of remote repositories
     *                     to resolve where to download the reference if the base_config flag doesn't
     *                     exist.
     */
    public Resolver(File cacheDirectory, List<Repository> repositories) {
        this.cacheDir = cacheDirectory;
        this.repositories = repositories;
    }

    /**
     * @param reference    the project reference to resolve to a remote reference.
     * @return a new reference to the remote file, or thows exception if it can't find it.
     */
    public Reference resolveRemote(Reference reference) {
        final File localCacheFile = cacheConfigFile(reference, cacheDir);

        if (localCacheFile.exists()) {
            return new RemoteReference(reference, localCacheFile);
        }
        else if (reference.getRemoteReference().isPresent()) {
            writeToFile(reference.getRemoteReference().get(), localCacheFile);
            return new RemoteReference(reference, localCacheFile);
        }
        else {
            URL url = makeUrl(reference, repositories);
            writeToFile(url, localCacheFile);
            return new RemoteReference(reference, localCacheFile);
        }
    }

    public File resolveBundle(BundleRef bundle) {
        final File file = cacheDir.toPath().resolve("bundles").resolve(bundle.getFileName()).toFile();

        if(file.exists()) {
            return file;
        }

        for (Repository repository : repositories) {
            if(bundle.isMavenSnapshot()) {
                final String snapshotMetadata = repository.getUrl() + bundle.toMavenSnapshotMetadata();

                if(isValidUrl(snapshotMetadata)) {
                    String metadataXml = downloadXml(snapshotMetadata);
                    final Matcher matcher = snapshotVersionPattern.matcher(metadataXml);

                    if(matcher.find()) {
                        final String snapshotUrl = repository.getUrl() + bundle.toSnapshotPath(matcher.group(1));

                        if (isValidUrl(snapshotUrl)) {
                            writeToFile(file, snapshotUrl);
                        }
                    }
                }
            }
            else {
                final String url = repository.getUrl() + bundle.toMavenPath();

                if (isValidUrl(url)) {
                    writeToFile(file, url);
                }
            }
        }

        if(file.exists() && file.length() > 1)
            return file;
        else
            throw new RuntimeException("Bundle not found: " + bundle + " in:"+ repositories);
    }

    private boolean writeToFile(File file, String spec2) {
        try(InputStream inputStream = Request.Get(spec2).execute().returnContent().asStream()) {
            file.getParentFile().mkdirs();
            Files.copy(inputStream, file.toPath());
            return true;
        }
        catch (IOException e) {
            // keep going
            System.out.println("Could not create file:"+e.getMessage());
            return false;
        }
    }

    private String downloadXml(String spec)  {
        try(InputStream is = Request.Get(spec).execute().returnContent().asStream()){
            return new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidUrl(String spec) {

        try {
            StatusLine statusLine = Request.Head(spec).execute().returnResponse().getStatusLine();
            return (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK);
        }
        catch (IOException e) {
            return false;
        }
    }

    private void writeToFile(URL inputUrl, File outputFile) {
        outputFile.getParentFile().mkdirs();

        if(inputUrl.getProtocol().equals("file")) {
            try(InputStream is = inputUrl.openStream()) {
                Files.copy(is, outputFile.toPath());
            }
            catch (IOException e) {
                // keep going
                System.err.println("could not copy file:" +e.getMessage());
            }
        }
        else {
            try(InputStream is = Request.Get(inputUrl.toString()).execute().returnContent().asStream()) {
                Files.copy(is, outputFile.toPath());
            }
            catch (IOException e) {
                // keep going
                System.err.println("could not download file"+ e.getMessage());
            }
        }
    }

    private URL makeUrl(Reference resource, List<Repository> repositories) {
        // todo test each repository

        try {
            return new URL(repositories.get(0).getUrl() + // todo enable for many repositories
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
