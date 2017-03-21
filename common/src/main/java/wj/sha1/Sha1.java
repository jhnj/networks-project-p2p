package wj.sha1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by johan on 21/03/17.
 */
public class Sha1 {
    // Return the hash for a block using SHA-1
    public static String SHAsum(byte[] block) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return byteArray2Hex(md.digest(block));
        } catch (NoSuchAlgorithmException n) {
            // Return empty string in case of error
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
