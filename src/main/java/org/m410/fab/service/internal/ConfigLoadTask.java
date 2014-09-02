package org.m410.fab.service.internal;

import org.m410.fab.builder.BuildContext;
import org.m410.fab.builder.Task;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.security.MessageDigest;
import java.util.Properties;

/**
 * @author m410
 */
public class ConfigLoadTask implements Task {
    @Override
    public String getName() {
        return "Create Hash";
    }

    @Override
    public String getDescription() {
        return "Used to check if the project configuration file needs to be reloaded";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        File projectProperties = FileSystems.getDefault()
                .getPath(context.getBuild().getCacheDir(), "project.properties")
                .toFile();

        if (!projectProperties.exists()) {
            File project = projectFile();
            save(project, projectProperties);
        }
        else {
            Properties properties = new Properties();


            try(FileInputStream in = new FileInputStream(projectProperties)) {
                properties.load(in);
            }

            context.cli().info(properties.toString());
        }
    }

    void save(File file, File projectProperties) throws Exception {
        Properties prop = new Properties();
        prop.put("checksum",getMD5Checksum(file));

        try(FileOutputStream out = new FileOutputStream(projectProperties)) {
            prop.store(out,"Project Properties");
        }

    }

    File projectFile() throws IOException {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.fab.yml");
        Path path = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
        return Files.walk(path, 1).filter(matcher::matches).findFirst().get().toFile();
    }

    static byte[] createChecksum(File file) throws Exception {
        InputStream fis = new FileInputStream(file);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fis.close();
        return complete.digest();
    }

    // see this How-to for a faster way to convert
    // a byte array to a HEX string
    static String getMD5Checksum(File filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";

        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }

        return result;
    }
}
