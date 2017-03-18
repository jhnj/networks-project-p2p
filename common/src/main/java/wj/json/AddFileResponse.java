package wj.json;

/**
 * Created by walter on 2017-03-18.
 */
public class AddFileResponse {
    private boolean ok;

    public AddFileResponse(boolean ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
