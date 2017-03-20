package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

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

    @JsonCreator
    public WJFile(@JsonProperty("name") String name,
                  @JsonProperty("size") int size,
                  @JsonProperty("hash") String hash,
                  @JsonProperty("blocks") String[] blocks) {
        this.name = name;
        this.size = size;
        this.hash = hash;
        this.blocks = blocks;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("size")
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    @JsonProperty("hash")
    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }

    @JsonProperty("blocks")
    public String[] getBlocks() {
        return blocks;
    }
    public void setBlocks(String[] blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WJFile wjFile = (WJFile) o;

        if (size != wjFile.size) return false;
        if (name != null ? !name.equals(wjFile.name) : wjFile.name != null) return false;
        if (hash != null ? !hash.equals(wjFile.hash) : wjFile.hash != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(blocks, wjFile.blocks);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + size;
        result = 31 * result + (hash != null ? hash.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(blocks);
        return result;
    }
}
