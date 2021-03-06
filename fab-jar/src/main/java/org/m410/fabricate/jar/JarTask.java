package org.m410.fabricate.jar;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class JarTask implements Task {

    public String getDescription() {
        return "Create a jar artifact";
    }

    public String getName() {
        return "Jar";
    }

    public void execute(BuildContext context) throws Exception {
        final ImmutableHierarchicalConfiguration config = context.getConfiguration();
        final String sourceOut = config.getString("build.source_output_dir");
        final File classseDir = Paths.get(sourceOut).toFile();
        final String destOut = config.getString("build.target_dir");
        final File targetFile = Paths.get(destOut).toFile();

        makeManifest(classseDir);

        if(!targetFile.exists() && !targetFile.mkdirs())
            throw new RuntimeException("could not make target dir");

        String name = context.getApplication().getName() + "-"+ context.getApplication().getVersion() +".jar";
        File zipFile = new File(targetFile, name);
        FileOutputStream fout = new FileOutputStream(zipFile);
        ZipOutputStream zout = new ZipOutputStream(fout);
        addDirectory(classseDir.toURI(), zout, classseDir);
        zout.close();
    }

    private void makeManifest(File classseDir) throws IOException {
        File metaInf = new File(classseDir,"META-INF");
        metaInf.mkdirs();
        File manifestMf = new File(metaInf,"MANIFEST.MF");

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(manifestMf))){
            writer.write("Manifest-Version: 1.0");
            writer.write("\n");
            writer.write("Created-By: fab(ricate) http://m410.org");
            writer.write("\n");
        }
    }

    private static void addDirectory(final URI base, final ZipOutputStream zout, final File sourceFile)
            throws IOException {
        byte[] buffer = new byte[1024];
        CRC32 crc = new CRC32();
        int bytesRead;

        for (File file : sourceFile.listFiles()) {

            if (file.isDirectory()) {
                addDirectory(base, zout, file);
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
            String pathName = base.relativize(file.toURI()).toString();
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
}
