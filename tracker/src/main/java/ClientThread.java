import java.net.Socket;

public class ClientThread implements Runnable {
    TrackerServer server;
    Socket socket;

    ClientThread(TrackerServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }


    public void run() {}

}
