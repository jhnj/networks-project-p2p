package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by johan on 25/03/17.
 */
public class BlockRequest {
    private String action;
    private String fileHash;
    private int blockIndex;

    public BlockRequest(String fileHash, int blockIndex) {
        this.action = "block";
        this.fileHash = fileHash;
        this.blockIndex = blockIndex;
    }

    @JsonCreator
    public BlockRequest(@JsonProperty("action")     String action,
                        @JsonProperty("file_hash")   String fileHash,
                        @JsonProperty("block_index")  int blockIndex) {
        this.action = action;
        this.fileHash = fileHash;
        this.blockIndex = blockIndex;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(int blockIndex) {
        this.blockIndex = blockIndex;
    }
}
