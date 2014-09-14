package org.m410.fab.garden;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

import java.io.*;
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


        try {
            File fileSource = FileSystems.getDefault().getPath(context.getBuild().getSourceOutputDir()).toFile();
            File targetDir = new File(context.getBuild().getTargetDir());
            File webDir = FileSystems.getDefault().getPath(context.getBuild().getWebappDir()).toFile();
            String cp = context.getClasspath().get("compile");
            File explodedDir = makeExploded(targetDir, fileSource,webDir, toFiles(cp));

            if(!explodedDir.exists() && !explodedDir.mkdirs())
                System.out.println("could not make target dir");

            String name = context.getApplication().getName() + "-"+ context.getApplication().getVersion() +".war";
            File zipFile = new File(targetDir, name);
            FileOutputStream fout = new FileOutputStream(zipFile);

            try (ZipOutputStream zout = new ZipOutputStream(fout)) {
                addDirectory(zout, explodedDir);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Collection<File> toFiles(String cp) {
        return Arrays.asList(cp.split(System.getProperty("path.separator")))
                .stream()
                .map(File::new)
                .collect(Collectors.toList());
    }

    private File makeExploded(File targetDir, File sourcesDir, File webDir, Collection<File> libs)
            throws IOException {
        File exploded = new File(targetDir,"war-exploded");
        Files.walkFileTree(webDir.toPath(), new CopyFileVisitor(exploded.toPath()));

        final File webInfDir = new File(exploded, "WEB-INF");

        File classesDir = new File(webInfDir,"classes");
        classesDir.mkdirs();
        Files.walkFileTree(sourcesDir.toPath(), new CopyFileVisitor(classesDir.toPath()));

        File libDir = new File(webInfDir,"lib");
        libDir.mkdirs();

        for (File lib : libs)
            Files.copy(lib.toPath(),new File(libDir, lib.getName()).toPath());

        return exploded;
    }

    private static void addDirectory(ZipOutputStream zout, File sourceFile) {
        assert sourceFile != null;
        byte[] buffer = new byte[1024];
        CRC32 crc = new CRC32();
        int bytesRead;

        for (File file : sourceFile.listFiles()) {

            if (file.isDirectory()) {
                addDirectory(zout, file);
                continue;
            }

            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                crc.reset();

                while ((bytesRead = bis.read(buffer)) != -1) {
                    crc.update(buffer, 0, bytesRead);
                }

                bis.close();
                // Reset to beginning of input stream
                bis = new BufferedInputStream(new FileInputStream(file));
                ZipEntry entry = new ZipEntry(file.getName());
                entry.setMethod(ZipEntry.STORED);
                entry.setCompressedSize(file.length());
                entry.setSize(file.length());
                entry.setCrc(crc.getValue());
                zout.putNextEntry(entry);

                while ((bytesRead = bis.read(buffer)) != -1) {
                    zout.write(buffer, 0, bytesRead);
                }

                bis.close();

            } catch (IOException ioe) {
                System.err.println("IOException :" + ioe);
            }
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
            }
            else {
                Files.createDirectories(targetPath.resolve(sourcePath
                        .relativize(dir)));
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                throws IOException {
            Files.copy(file, targetPath.resolve(sourcePath.relativize(file)));
            return FileVisitResult.CONTINUE;
        }
    }

}
