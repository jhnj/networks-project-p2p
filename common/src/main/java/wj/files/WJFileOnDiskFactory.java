package wj.files;

import wj.sha1.Sha1;

import java.io.*;

import static wj.files.WJFileOnDisk.BLOCK_SIZE;

/**
 * Created by johan on 21/03/17.
 * Factory for initializing files
 */
public class WJFileOnDiskFactory {
    public static WJFileOnDisk initiateFile(String name, String path) throws IOException {
        File file = new File(path);
        long size = file.length();

        byte[] block = new byte[BLOCK_SIZE];
        // Array length: number of blocks rounded up
        String[] blockHashes = new String[(int) ((file.length() + BLOCK_SIZE - 1) / BLOCK_SIZE)];

        InputStream inputStream = new FileInputStream(file);
        int bytesRead = inputStream.read(block);
        int i = 0;

        while (bytesRead != -1) {
            blockHashes[i] = Sha1.SHAsum(block);

            // reinitialize array with 0
            block = new byte[BLOCK_SIZE];
            bytesRead = inputStream.read(block);
            i++;
        }

        String hash = Sha1.SHAsum(blockHashes);

        return new WJFileOnDisk(name, size, hash, blockHashes, path);
    }
}
