import java.net.InetAddress;

/**
 * Created by walter on 2017-03-18.
 */
public class User {
    private InetAddress ip;
    private int port;

    public User(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (port != user.port) return false;
        return ip != null ? ip.equals(user.ip) : user.ip == null;

    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }
}
