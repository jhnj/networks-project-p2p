package wj.files;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.nashorn.internal.ir.Block;
import wj.exceptions.BlockException;
import wj.json.WJFile;
import wj.sha1.Sha1;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by johan on 21/03/17.
 */
public class WJFileOnDisk extends WJFile {
    public static final short BLOCK_SIZE = Short.MAX_VALUE;

    private String path;
    private File file;
    private RandomAccessFile rf;
    private boolean[] blocksOnDisk;

    public WJFileOnDisk(@JsonProperty("name") String name,
                        @JsonProperty("size") long size,
                        @JsonProperty("hash") String hash,
                        @JsonProperty("blocks") String[] blocks,
                        String path) {
        super(name, size, hash, blocks);
        this.path = path;
        this.file = new File(path);
        blocksOnDisk = new boolean[blocks.length];
    }

    @JsonIgnore
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        this.file = new File(this.path);
    }

    /**
     * Used to set all blocks to found for a locally uploaded file
     */
    @JsonIgnore
    public void setLocal() {
        Arrays.fill(blocksOnDisk, true);
    }

    @JsonIgnore
    public boolean[] getBlocksOnDisk() {
        return blocksOnDisk;
    }

    @JsonIgnore
    public Boolean blockFound(int block) {
        if (block < 0 || block >= blocksOnDisk.length) {
            return false;
        }
        return blocksOnDisk[block];
    }

    @JsonIgnore
    public int getBlockNumber(String blockHash) {
        return Arrays.asList(this.getBlocks()).indexOf(blockHash);
    }

    @JsonIgnore
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

        if (!this.getBlocks()[block].equals(Sha1.SHAsum(data))) {
            throw new BlockException("Invalid hash");
        }

        return data;
    }

    @JsonIgnore
    public void writeBlock(int block, byte[] data) throws IOException, BlockException {
        if (block >= this.getBlocks().length || block < 0) {
            throw new BlockException("Invalid block number");
        }

        if (this.blocksOnDisk[block]) {
            throw new BlockException("Block already on disk");
        }

        if (!this.getBlocks()[block].equals(Sha1.SHAsum(data))) {
            throw new BlockException("Invalid hash");
        }

        try (RandomAccessFile rf = new RandomAccessFile(file, "r")) {
            rf.seek(block * BLOCK_SIZE);
            rf.write(data);
        }
    }

}
