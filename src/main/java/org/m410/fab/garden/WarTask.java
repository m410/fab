package org.m410.fab.garden;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

import java.io.*;
import java.nio.file.FileSystems;
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
            File targetFile = new File(context.getBuild().getTargetDir());

            writeManifestTo(fileSource);
            writeWebXml(fileSource);

            if(!targetFile.exists() && !targetFile.mkdirs())
                System.out.println("could not make target dir");

            String name = context.getApplication().getName() + "-"+ context.getApplication().getVersion() +".war";
            File zipFile = new File(targetFile, name);
            FileOutputStream fout = new FileOutputStream(zipFile);
            ZipOutputStream zout = new ZipOutputStream(fout);

            addDirectory(zout, fileSource);
            zout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeWebXml(File fileSource) throws IOException {
        File dir = new File(fileSource,"WEB-INF");
        dir.mkdirs();
        File manifest = new File(dir,"web.xml");

        try(FileWriter write = new FileWriter(manifest)) {
            write.write("<web-app xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" +
                    "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "      xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\"\n" +
                    "      version=\"3.0\" metadata-complete=\"true\"> ");
            write.write("\n");
            write.write("  <listener-class>");
            write.write("org.m410.garden.application.ApplicationContextListener");
            write.write("</listener-class>");
            write.write("\n");
            write.write("  <context-param>");
            write.write("\n");
            write.write("    <param-name>m410-env</param-name>");
            write.write("\n");
            write.write("    <param-value>production</param-value>");
            write.write("\n");
            write.write("  </context-param>");
            write.write("\n");
            write.write("</web-app>");
        }
    }

    private void writeManifestTo(File fileSource) throws IOException {
        File dir = new File(fileSource,"META-INF");
        dir.mkdirs();
        File manifest = new File(dir,"MANIFEST.MF");

        try(FileWriter write = new FileWriter(manifest)) {
            write.write("Manifest-Version: 1.0");
            write.write("\n");
            write.write("Created-By: org.m410.fabricate");
            write.write("\n");
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
                System.err.println("IOException :" + ioe);
            }
        }
    }
}
