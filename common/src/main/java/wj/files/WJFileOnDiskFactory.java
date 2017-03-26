package wj.files;

import wj.json.WJFile;
import wj.sha1.Sha1;

import java.io.*;

import static wj.files.WJFileOnDisk.BLOCK_SIZE;

/**
 * Created by johan on 21/03/17.
 * Factory for initializing files
 */
public class WJFileOnDiskFactory {
    public static WJFileOnDisk initiateLocalFile(String name, String path) throws IOException {
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

        WJFileOnDisk wjFileOnDisk =  new WJFileOnDisk(name, size, hash, blockHashes, path);
        wjFileOnDisk.setLocal();
        return wjFileOnDisk;
    }

    /** Instantiates a WJFileOnDisk based on a remote WJFile
     *
     *  Creates an empty file with the same name as the WJFile */
    public static WJFileOnDisk initiateRemoteFile(WJFile file) throws IOException {
        File diskFile = new File(file.getName());
        boolean fileAdded = diskFile.createNewFile();
        if (!fileAdded) {
            throw new IOException("Unable to add the file locally");
        }

        return new WJFileOnDisk(file.getName(), file.getSize(), file.getHash(), file.getBlocks(), diskFile.getAbsolutePath());
    }
}
