package org.m410.fabricate.war;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * @author m410
 */
public class WarTask implements Task {
    @Override
    public String getName() {
        return "war";
    }

    @Override
    public String getDescription() {
        return "Create a java war artifact";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        final String sourceOutputDir = context.getConfiguration().getString("build.sourceOutputDir");
        final File baseDir = Paths.get(sourceOutputDir).toFile();
        final File targetDir = new File(context.getConfiguration().getString("build.webappOutput"));
        final String webappDir = context.getConfiguration().getString("build.webappDir");
        final File webDir = FileSystems.getDefault().getPath(webappDir).toFile();

        String cp = context.getClasspath().get("compile");
        File explodedDir = makeExploded(targetDir, baseDir, webDir, toFiles(cp));
        makeManifest(explodedDir);

        if (!explodedDir.exists() && !explodedDir.mkdirs())
            throw new RuntimeException("could not make target dir");

        String name = context.getApplication().getName() + "-" + context.getApplication().getVersion() + ".war";
        File zipFile = new File(targetDir, name);
        FileOutputStream fout = new FileOutputStream(zipFile);

        try (ZipOutputStream zout = new ZipOutputStream(fout)) {
            addDirectory(explodedDir.toURI(), zout, explodedDir);
        }
    }

    private void makeManifest(File classseDir) throws IOException {
        File metaInf = new File(classseDir,"META-INF");
        metaInf.mkdir();
        File manifestMf = new File(metaInf,"MANIFEST.MF");

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(manifestMf))){
            writer.write("Manifest-Version: 1.0");
            writer.write("\n");
            writer.write("Created-By: fab(ricate) http://m410.org/fabricate");
            writer.write("\n");
        }
    }


    private Collection<File> toFiles(String cp) {
        return Arrays.stream(cp.split(System.getProperty("path.separator")))
                .map(File::new)
                .collect(Collectors.toList());
    }

    private File makeExploded(File exploded, File sourcesDir, File webDir, Collection<File> libs)
            throws IOException {

        if(!exploded.exists() && !exploded.mkdirs())
            throw new RuntimeException("Could not make war-exploded directory");

        Files.walkFileTree(webDir.toPath(), new CopyFileVisitor(exploded.toPath()));
        final File webInfDir = new File(exploded, "WEB-INF");
        final File classesDir = new File(webInfDir, "classes");
        classesDir.mkdirs();
        Files.walkFileTree(sourcesDir.toPath(), new CopyFileVisitor(classesDir.toPath()));

        File libDir = new File(webInfDir, "lib");
        libDir.mkdirs();

        for (File lib : libs)
            Files.copy(lib.toPath(), new File(libDir, lib.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);

        return exploded;
    }

    private static void addDirectory(final URI baseUri, final ZipOutputStream zout, final File sourceFile)
            throws IOException {
        assert sourceFile != null;
        byte[] buffer = new byte[1024];
        CRC32 crc = new CRC32();
        int bytesRead;

        for (File file : sourceFile.listFiles()) {

            if (file.isDirectory()) {
                addDirectory(baseUri, zout, file);
                continue;
            }

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            crc.reset();

            while ((bytesRead = bis.read(buffer)) != -1) {
                crc.update(buffer, 0, bytesRead);
            }

            bis.close();
            // Reset to beginning of input stream
            bis = new BufferedInputStream(new FileInputStream(file));
            String pathName = baseUri.relativize(file.toURI()).toString();
            ZipEntry entry = new ZipEntry(pathName);
            entry.setMethod(ZipEntry.STORED);
            entry.setCompressedSize(file.length());
            entry.setSize(file.length());
            entry.setCrc(crc.getValue());
            zout.putNextEntry(entry);

            while ((bytesRead = bis.read(buffer)) != -1) {
                zout.write(buffer, 0, bytesRead);
            }

            bis.close();
        }
    }

    public class CopyFileVisitor extends SimpleFileVisitor<Path> {
        private final Path targetPath;
        private Path sourcePath = null;

        public CopyFileVisitor(Path targetPath) {
            this.targetPath = targetPath;
        }

        @Override
        public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                throws IOException {
            if (sourcePath == null) {
                sourcePath = dir;
            } else {
                Files.createDirectories(targetPath.resolve(sourcePath
                        .relativize(dir)));
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                throws IOException {
            Files.copy(file, targetPath.resolve(sourcePath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
            return FileVisitResult.CONTINUE;
        }
    }

}
