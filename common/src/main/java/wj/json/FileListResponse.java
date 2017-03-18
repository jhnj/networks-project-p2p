package wj.json;

/**
 * Created by walter on 2017-03-18.
 */
public class FileListResponse {
    private WJFile[] files;

    public FileListResponse(WJFile[] files) {
        this.files = files;
    }

    public WJFile[] getFiles() {
        return files;
    }

    public void setFiles(WJFile[] files) {
        this.files = files;
    }
}
