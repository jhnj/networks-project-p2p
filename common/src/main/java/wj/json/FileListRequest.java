package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by walter on 2017-03-18.
 */
public class FileListRequest {
    private String action;
    private WJFile[] existing_files;

    public FileListRequest(WJFile[] existing_files) {
        this.action = "file_list";
        this.existing_files = existing_files;
    }

    @JsonCreator
    public FileListRequest(@JsonProperty("action") String action,
                           @JsonProperty("existing_files") WJFile[] existing_files) {
        this.action = action;
        this.existing_files = existing_files;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public WJFile[] getExisting_files() {
        return existing_files;
    }

    public void setExisting_files(WJFile[] existing_files) {
        this.existing_files = existing_files;
    }
}
