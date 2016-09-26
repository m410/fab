package org.m410.fabricate.global;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.http.client.fluent.Request;
import org.m410.config.YamlConfiguration;
import org.m410.fabricate.CopyFileVisitor;
import org.m410.fabricate.ZipUtil;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

/**
 * @author Michael Fortin
 */
public class CreateCmd implements Runnable {
    String name[];
    DataStore dataStore;

    public CreateCmd(DataStore dataStore, String[] name) {
        this.name = name;
        this.dataStore = dataStore;
    }

    @Override
    public void run() {
        if (name.length == 2) {
            doCli();
        }
    }

    private void doCli() {
        final Optional<ImmutableHierarchicalConfiguration> byName = dataStore.findByName(name[1]);
        Scanner scanner = new Scanner(System.in);
        System.out.print(" name: ");
        String name = scanner.next();
        System.out.print(" org: ");
        String org = scanner.next();
        System.out.print(" version: ");
        String version = scanner.next();
        System.out.println("");
        System.out.println(name + ":" + org + ":" + version);

        final ImmutableHierarchicalConfiguration config = byName.get();
        final String templateUrl = config.getString("versions(0).url");

        Path downloadedTemplate = downloadTemplate(templateUrl);
        Path local = copyToWorkingDir(name, downloadedTemplate);
        updateConfiguration(local, name, org, version);
        renamePackages(local, name, org, version);
    }

    private void renamePackages(Path local, String name, String org, String version) {
        // todo implement me

        //        Path fileToMovePath =
        //                Files.createFile(Paths.get("src/test/resources/" + randomAlphabetic(5) + ".txt"));
        //        Path targetPath = Paths.get("src/main/resources/");
        //
        //        Files.move(fileToMovePath, targetPath.resolve(fileToMovePath.getFileName()));
    }

    private void updateConfiguration(Path localPath, String name, String org, String version) {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.fab.yml");
        final Optional<Path> first;

        try {
            first = Files.walk(localPath, 1).filter(matcher::matches).findFirst();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!first.isPresent()) {
            System.err.println("couldn't find configuration");
            System.exit(1);
        }

        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        first.ifPresent(f -> {
            try (FileReader reader = new FileReader(f.toFile())) {
                yamlConfiguration.read(reader);
            }
            catch (IOException | ConfigurationException e) {
                throw new RuntimeException(e);
            }
        });

        yamlConfiguration.setProperty("application.name", name);
        yamlConfiguration.setProperty("application.org", org);
        yamlConfiguration.setProperty("application.version", version);

        try (FileWriter writer = new FileWriter(first.get().toFile())) {
            yamlConfiguration.write(writer);
        }
        catch (IOException | ConfigurationException e) {
            System.err.println("Could not write configuration:" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private Path copyToWorkingDir(String name, Path downloadedTemplate) {
        final File file = new File(name);
        file.mkdir();
        final Path localPath = file.toPath();
        final File[] childFiles = downloadedTemplate.toFile().listFiles();
        System.out.println(Arrays.toString(childFiles));

        if (childFiles != null && childFiles.length == 1) {
            final File downloadedName = childFiles[0];
            final String nameOfDownload = downloadedName.getName();
            System.out.println("name of download:" + nameOfDownload);

            try {
                Files.walkFileTree(downloadedName.toPath(), new CopyFileVisitor(localPath));
            }
            catch (IOException e) {
                System.err.println("Could not copy project:" + e.getMessage());
                e.printStackTrace();
                System.exit(1);
                return null;
            }
        }

        return localPath;
    }

    private Path downloadTemplate(String templateUrl) {

        try (InputStream inputStream = Request.Get(templateUrl).execute().returnContent().asStream()) {
            File tmpZip = File.createTempFile("fab-create-", ".zip");
            final Path tempDirectory = Files.createTempDirectory("fab-create-");
            tmpZip.delete();

            Files.copy(inputStream, tmpZip.toPath());
            ZipUtil.extract(tmpZip, tempDirectory.toFile());

            return tempDirectory;
        }
        catch (IOException e) {
            System.err.println("Could not create file:" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}