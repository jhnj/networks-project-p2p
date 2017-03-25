package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by walter on 2017-03-18.
 */
public class WJClient {
    private String ipString;
    private InetAddress ip;
    private int port;

    @JsonCreator
    public WJClient(@JsonProperty("ip")   String ipString,
                    @JsonProperty("port") int port) throws UnknownHostException {
        this.setIp(ipString);
        this.setPort(port);
    }

    @JsonProperty("ip")
    public String getIp() {
        return ipString;
    }
    public void setIp(String ip) throws UnknownHostException {
        this.ip = InetAddress.getByName(ip);
        this.ipString = ip;
    }

    @JsonProperty("port")
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }

    @JsonIgnore
    public InetAddress getInetAddress() {
        return ip;
    }

    @JsonIgnore
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WJClient user = (WJClient) o;

        if (port != user.port) return false;
        return ip != null ? ip.equals(user.ip) : user.ip == null;

    }

    @JsonIgnore
    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }
}
