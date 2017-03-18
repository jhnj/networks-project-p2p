package wj_json;

/**
 * Created by johan on 18/03/17.
 * FILE json object type
 */
public class WJFile {
    private String name;
    // File size in bytes
    private int size;
    // The SHA-1 hash of the file
    private String hash;
    // An array containing the block hashes of the file
    private String[] blocks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String[] getBlocks() {
        return blocks;
    }

    public void setBlocks(String[] blocks) {
        this.blocks = blocks;
    }
}
