package org.m410.fab.garden;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

import java.io.*;
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
                System.out.println("could not make target dir");

            String name = context.getApplication().getName() + "-"+ context.getApplication().getVersion() +".jar";
            File zipFile = new File(targetFile, name);
            FileOutputStream fout = new FileOutputStream(zipFile);
            ZipOutputStream zout = new ZipOutputStream(fout);
            File fileSource = FileSystems.getDefault().getPath(context.getBuild().getSourceOutputDir()).toFile();
            addDirectory(zout, fileSource);
            zout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                System.out.println("IOException :" + ioe);
            }
        }
    }
}
