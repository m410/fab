package org.m410.fab.compiler;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

import java.io.*;
import java.util.zip.*;

public final class JarTask implements Task{

    public String getDescription() {
        return "Create a jar";
    }

    public String getName() {
        return "Jar";
    }

    public void execute(BuildContext context)  {

      try {
	File zipFile = new File("test.zip");
        String[] args = new String[]{};

        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);
        int bytesRead;
        byte[] buffer = new byte[1024];
        CRC32 crc = new CRC32();
        for (int i=1, n=args.length; i < n; i++) {
            String name = args[i];
            File file = new File(name);
            if (!file.exists()) {
                System.err.println("Skipping: " + name);
                continue;
            }
            BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(file));
            crc.reset();
            while ((bytesRead = bis.read(buffer)) != -1) {
                crc.update(buffer, 0, bytesRead);
            }
            bis.close();
            // Reset to beginning of input stream
            bis = new BufferedInputStream(
                new FileInputStream(file));
            ZipEntry entry = new ZipEntry(name);
            entry.setMethod(ZipEntry.STORED);
            entry.setCompressedSize(file.length());
            entry.setSize(file.length());
            entry.setCrc(crc.getValue());
            zos.putNextEntry(entry);
            while ((bytesRead = bis.read(buffer)) != -1) {
                zos.write(buffer, 0, bytesRead);
            }
            bis.close();
        }
        zos.close();
      }
      catch(Exception e) {
          e.printStackTrace();
      }
    }
}
