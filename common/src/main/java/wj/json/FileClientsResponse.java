package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by walter on 2017-03-21.
 */
public class FileClientsResponse {
    private WJClient[] clients;

    @JsonCreator
    public FileClientsResponse(@JsonProperty("clients") WJClient[] clients) {
        this.clients = clients;
    }

    public WJClient[] getClients() {
        return clients;
    }

    public void setClients(WJClient[] clients) {
        this.clients = clients;
    }
}
