package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by johan on 26/03/17.
 */
public class SetupPortRequest {
    private String action;
    private int port;

    public SetupPortRequest(int port) {
        this.action = "file_clients";
        this.port = port;
    }

    @JsonCreator
    public SetupPortRequest(@JsonProperty("action") String action,
                            @JsonProperty("port")   int port) {
        this.action = action;
        this.port = port;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
