package org.m410.fabricate.config;


import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    static final Pattern snapshotJarVersionPattern = Pattern.compile("(?smi)<extension>jar</extension>.*?<value>(.*?)" +
                                                                     "</value>");
    static final Pattern snapshotYmlVersionPattern = Pattern.compile("(?smi)<extension>yml</extension>.*?<value>(.*?)" +
                                                                     "</value>");

    private File cacheDir;
    private List<Repository> repositories;

    /**
     * @param cacheDirectory
     * @param repositories placeholder arg, should list of remote repositories
     *                     to resolve where to download the reference if the base_config flag doesn't
     *                     exist.
     */
    Resolver(File cacheDirectory, List<Repository> repositories) {
        this.cacheDir = cacheDirectory;
        this.repositories = repositories;
    }

    /**
     * @param reference    the project reference to resolve to a remote reference.
     * @return a new reference to the remote file, or thows exception if it can't find it.
     */
    public Reference resolveRemote(Reference reference) {
        final File localCacheFile = cacheConfigFile(reference, cacheDir);
        localCacheFile.getParentFile().mkdirs();

        if (localCacheFile.exists()) {
            return new RemoteReference(reference, localCacheFile);
        }
        else if (reference.getRemoteReference().isPresent()) {
            explicitRemoteReference(reference, localCacheFile);
            return new RemoteReference(reference, localCacheFile);
        }
        else {
            tryRemoteRepos(reference, localCacheFile);

            if (!localCacheFile.exists()) {
                throw new RuntimeException("file not retrieved: " + reference.toMavenPath());
            }

            return new RemoteReference(reference, localCacheFile);
        }
    }

    public File resolveBundle(BundleRef bundle) {
        final File file = cacheDir.toPath().resolve("bundles").resolve(bundle.getFileName()).toFile();
        file.getParentFile().mkdirs();

        if (file.exists() && file.length() > 1) {
            return file;
        }

        explicitRemoteReference(bundle, file);

        if (file.exists() && file.length() > 1) {
            return file;
        }

        tryRemoteRepos(bundle, file);

        if (file.exists() && file.length() > 1) {
            return file;
        }
        else {
            throw new RuntimeException("Bundle not found: " + bundle + " in:" + repositories);
        }
    }

    private void explicitRemoteReference(Reference bundle, File file) {
        bundle.getRemoteReference().ifPresent(url -> {
            try (InputStream is = url.openStream()) {
                Files.copy(is, file.toPath());
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void tryRemoteRepos(Reference bundle, File file) {
        for (Repository repository : repositories) {
            if(bundle.isMavenSnapshot()) {
                final String snapshotMetadata = repository.getUrl() + bundle.toMavenSnapshotMetadata();

                if(isValidUrl(snapshotMetadata)) {
                    String metadataXml = downloadXml(snapshotMetadata);
                    //                    System.out.println("metadata:" + metadataXml);
                    final Matcher matcher = bundle.toMavenPath().endsWith("yml") ?
                                            snapshotYmlVersionPattern.matcher(metadataXml) :
                                            snapshotJarVersionPattern.matcher(metadataXml);

                    if(matcher.find()) {
                        final String snapshotUrl = repository.getUrl() + bundle.toSnapshotPath(matcher.group(1));
                        //                        System.out.println("snapshot url:" + snapshotUrl);

                        if (isValidUrl(snapshotUrl)) {
                            writeToFile(file, snapshotUrl);
                        }
                    }
                }
            }
            else {
                final String url = repository.getUrl() + bundle.toMavenPath();
                System.out.println("url:" + url);

                if (isValidUrl(url)) {
                    writeToFile(file, url);
                }
            }
        }
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
