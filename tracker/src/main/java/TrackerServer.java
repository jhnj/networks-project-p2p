import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by walter on 2017-03-18.
 */
public class TrackerServer {
    ServerSocket serverSocket;

    public TrackerServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void listen() {
        System.out.println("Listening for connections on port " + this.serverSocket.getLocalPort() + "...");
        try {
            Socket socket = this.serverSocket.accept();
            System.out.println("Accepted a connection from " + socket.getInetAddress().toString());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
