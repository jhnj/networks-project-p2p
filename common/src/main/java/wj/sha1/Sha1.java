package wj.sha1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

/**
 * Created by johan on 21/03/17.
 */
public class Sha1 {
    // Return the hash for a block in hexadecimal using SHA-1
    public static String SHAsum(byte[] block) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return byteArray2Hex(md.digest(block));
        } catch (NoSuchAlgorithmException n) {
            // Return empty string in case of error
            return "";
        }
    }

    // Calculate the combined hash of multiple SHA-1 hashes in hexadecimal
    public static String SHAsum(String[] blockHashes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            // SHA-1 hash is 160 bytes long
            byte[] data = new byte[40 * blockHashes.length];
            for (int i = 0; i < blockHashes.length; i++) {
                int hashLen = blockHashes[i].length();
                byte[] test = blockHashes[i].getBytes();

                System.arraycopy(blockHashes[i].getBytes(), 0, data, i * 40, 40);
            }

            return byteArray2Hex(md.digest(data));
        } catch (NoSuchAlgorithmException n) {
            // Empty string in case of error
            return "";
        }
    }

    private static String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
