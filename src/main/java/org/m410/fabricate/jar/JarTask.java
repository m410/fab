package org.m410.fabricate.jar;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.zip.*;

public final class JarTask implements Task {

    public String getDescription() {
        return "Create a jar artifact";
    }

    public String getName() {
        return "Jar";
    }

    public void execute(BuildContext context) throws Exception {

        // todo need to create META-INF/MANIFEST.MF
        // Manifest-Version: 1.0
        // Created-By: 1.7.0_06 (Oracle Corporation)


        try {
            File targetFile = new File(context.getBuild().getTargetDir());

            if(!targetFile.exists() && !targetFile.mkdirs())
                throw new RuntimeException("could not make target dir");

            String name = context.getApplication().getName() + "-"+ context.getApplication().getVersion() +".jar";
            File zipFile = new File(targetFile, name);
            FileOutputStream fout = new FileOutputStream(zipFile);
            ZipOutputStream zout = new ZipOutputStream(fout);
            File fileSource = FileSystems.getDefault().getPath(context.getBuild().getSourceOutputDir()).toFile();
            addDirectory(fileSource.toURI(), zout, fileSource);
            zout.close();
        } catch (Exception e) {
            e.printStackTrace();
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
