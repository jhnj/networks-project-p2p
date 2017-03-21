package wj.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.nashorn.internal.ir.Block;
import wj.exceptions.BlockException;
import wj.json.WJFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by johan on 21/03/17.
 */
public class WJFileOnDisk extends WJFile {
    public static final short BLOCK_SIZE = Short.MAX_VALUE;

    private String path;
    private File file;
    private RandomAccessFile rf;
    private Boolean[] blocksOnDisk;

    public WJFileOnDisk(@JsonProperty("name") String name,
                        @JsonProperty("size") long size,
                        @JsonProperty("hash") String hash,
                        @JsonProperty("blocks") String[] blocks,
                        String path) {
        super(name, size, hash, blocks);
        this.path = path;
        this.file = new File(path);
        blocksOnDisk = new Boolean[blocks.length];
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        this.file = new File(this.path);
    }

    public byte[] readBlock(int block) throws BlockException, IOException {
        byte[] data = new byte[BLOCK_SIZE];
        if (block >= this.getBlocks().length || block < 0) {
            throw new BlockException("Invalid block number");
        }

        if (!this.blocksOnDisk[block]) {
            throw new BlockException("Block not on disk");
        }
        try (RandomAccessFile rf = new RandomAccessFile(file, "r")) {
            rf.seek(block * BLOCK_SIZE);
            rf.read(data);
        }

        return data;
    }

    public void writeBlock(int block, byte[] data) throws IOException, BlockException {
        if (block >= this.getBlocks().length || block < 0) {
            throw new BlockException("Invalid block number");
        }

        if (this.blocksOnDisk[block]) {
            throw new BlockException("Block already on disk");
        }

        try (RandomAccessFile rf = new RandomAccessFile(file, "r")) {
            rf.seek(block * BLOCK_SIZE);
            rf.write(data);
        }
    }

}
