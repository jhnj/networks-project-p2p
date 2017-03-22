package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by walter on 2017-03-22.
 */
public class BlockListRequest {
    private String action;
    private String file;

    public BlockListRequest(String file) {
        this.action = "block_list";
        this.file = file;
    }

    @JsonCreator
    public BlockListRequest(@JsonProperty("action") String action,
                            @JsonProperty("file")   String file) {
        this.action = action;
        this.file = file;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
