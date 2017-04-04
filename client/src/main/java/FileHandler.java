import wj.exceptions.BlockException;
import wj.exceptions.WJException;
import wj.files.WJFileOnDisk;
import wj.json.WJFile;

import java.io.IOException;
import java.util.*;

import static wj.files.WJFileOnDiskFactory.*;

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

    public int[] getBlockList(String hash) {
        WJFileOnDisk fileOnDisk = this.files.get(hash);
        if (fileOnDisk == null) {
            return new int[0];
        }

        ArrayList<Integer> blocks = new ArrayList<>();
        int i = 0;
        for (Boolean blockFound : fileOnDisk.getBlocksOnDisk()) {
            if (blockFound) {
                blocks.add(i);
            }
            i++;
        }

        return toPrimitiveIntArray(blocks);
    }

    /**
     * ArrayList to primitive array
     */
    private int[] toPrimitiveIntArray(ArrayList<Integer> array)  {
        int[] ret = new int[array.size()];
        int i = 0;
        for (Integer e : array)
            ret[i++] = e;
        return ret;
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

    public byte[] getBlock(String hash, int blockIndex) throws IOException, BlockException {
        WJFileOnDisk fileOnDisk = this.files.get(hash);
        if (fileOnDisk == null) {
            throw new IOException("File not found");
        }
        return fileOnDisk.readBlock(blockIndex);
    }

    public boolean addLocalFile(String name, String path) throws IOException, WJException {
        WJFileOnDisk fileOnDisk = initiateLocalFile(name, path);
        // return false if file already present
        if (files.containsKey(fileOnDisk.getHash())) {
            return false;
        }

        session.addFile(fileOnDisk);
        files.put(fileOnDisk.getHash(), fileOnDisk);
        return true;
    }

    public boolean addRemoteFile(WJFile file) throws IOException {
        WJFileOnDisk fileOnDisk = initiateRemoteFile(file);
        files.put(fileOnDisk.getHash(), fileOnDisk);
        return true;
    }
}
