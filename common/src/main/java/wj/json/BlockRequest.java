package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by johan on 25/03/17.
 */
public class BlockRequest {
    private String action;
    private String fileHash;
    private String blockHash;

    public BlockRequest(String fileHash, String blockHash) {
        this.action = "block";
        this.fileHash = fileHash;
        this.blockHash = blockHash;
    }

    @JsonCreator
    public BlockRequest(@JsonProperty("action")     String action,
                        @JsonProperty("fileHash")   String fileHash,
                        @JsonProperty("blockHash")  String blockHash) {
        this.action = action;
        this.fileHash = fileHash;
        this.blockHash = blockHash;
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

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }
}
