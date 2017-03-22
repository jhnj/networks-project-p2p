import wj.exceptions.BlockException;
import wj.exceptions.WJException;
import wj.files.WJFileOnDisk;
import wj.files.WJFileOnDiskFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static wj.files.WJFileOnDiskFactory.initiateFile;

/**
 * Created by johan on 22/03/17.
 */
public class FileHandler {
    private Map<String, WJFileOnDisk> files;
    ClientSession session;

    public FileHandler(ClientSession session) {
        this.session = session;
        this.files = Collections.synchronizedMap(new HashMap<>());
    }

    public Boolean storeBlock(String hash, int block, byte[] data) {
        WJFileOnDisk fileOnDisk  = this.files.get(hash);
        if (fileOnDisk == null) {
            return false;
        }

        try {
            fileOnDisk.writeBlock(block, data);
        } catch (IOException io) {
            System.err.println("IOException: " + io.getMessage());
            return false;
        } catch (BlockException b) {
            System.err.println("BlockException: " + b.getMessage());
            return false;
        }
        return true;
    }

    public byte[] getBlock(String hash, int block) throws IOException, BlockException {
        WJFileOnDisk fileOnDisk = this.files.get(hash);
        if (fileOnDisk == null) {
            throw new IOException("File not found");
        }
        return fileOnDisk.readBlock(block);
    }

    public boolean addFile(String name, String path) throws IOException, WJException {
        WJFileOnDisk fileOnDisk = initiateFile(name, path);
        // return false if file already present
        if (files.containsKey(fileOnDisk.getHash())) {
            return false;
        }

        session.addFile(fileOnDisk);
        files.put(fileOnDisk.getHash(), fileOnDisk);
        return true;
    }
}
