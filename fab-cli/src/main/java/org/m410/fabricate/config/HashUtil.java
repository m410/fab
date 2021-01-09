package org.m410.fabricate.config;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author m410
 */
public class HashUtil {

//    public static File projectFile() throws IOException {
//        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.fab.yml");
//        Path path = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
//        return Files.walk(path, 1).filter(matcher::matches).findFirst().get().toFile();
//    }

    // see this How-to for a faster way to convert
    // a byte array to a HEX string
    public static String getMD5Checksum(File filename) {
        byte[] b = createChecksum(filename);
        String result = "";

        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }

        return result;
    }


    private static byte[] createChecksum(File file) {
        try {
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
        catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
