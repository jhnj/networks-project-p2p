package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by walter on 2017-03-18.
 */
public class FileListResponse {
    private WJFile[] files;

    @JsonCreator
    public FileListResponse(@JsonProperty("files") WJFile[] files) {
        this.files = files;
    }

    public WJFile[] getFiles() {
        return files;
    }

    public void setFiles(WJFile[] files) {
        this.files = files;
    }
}
