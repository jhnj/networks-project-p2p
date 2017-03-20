package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import wj.json.WJFile;

/**
 * Created by walter on 2017-03-18.
 */
public class AddFileRequest {
    private String action;
    private WJFile file;

    public AddFileRequest(WJFile file) {
        this.action = "add_file";
        this.file = file;
    }

    @JsonCreator
    public AddFileRequest(@JsonProperty("action") String action,
                          @JsonProperty("file") WJFile file) {
        this.action = action;
        this.file = file;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public WJFile getFile() {
        return file;
    }

    public void setFile(WJFile file) {
        this.file = file;
    }
}
