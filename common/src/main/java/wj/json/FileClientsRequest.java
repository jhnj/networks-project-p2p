package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by walter on 2017-03-21.
 */
public class FileClientsRequest {
    private String action;
    private WJFile file;

    public FileClientsRequest(WJFile file) {
        this.action = "file_clients";
        this.file = file;
    }

    @JsonCreator
    public FileClientsRequest(@JsonProperty("action") String action,
                              @JsonProperty("file")   WJFile file) {
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
