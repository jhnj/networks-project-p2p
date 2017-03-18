import java.net.Socket;

public class ClientThread implements Runnable {
    private TrackerServer server;
    private Socket socket;

    ClientThread(TrackerServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }


    public void run() {}

}
