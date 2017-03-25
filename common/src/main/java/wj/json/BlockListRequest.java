package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by walter on 2017-03-22.
 */
public class BlockListRequest {
    private String action;
    private String fileHash;

    public BlockListRequest(String file) {
        this.action = "block_list";
        this.fileHash = file;
    }

    @JsonCreator
    public BlockListRequest(@JsonProperty("action")     String action,
                            @JsonProperty("fileHash")   String fileHash) {
        this.action = action;
        this.fileHash = fileHash;
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

    public void setFileHash(String file) {
        this.fileHash = file;
    }
}
