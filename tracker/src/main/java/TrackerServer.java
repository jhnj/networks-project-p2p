import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by walter on 2017-03-18.
 */
public class TrackerServer {
    ServerSocket serverSocket;
    private CopyOnWriteArrayList<File> files;

    public TrackerServer(int port) {
        this.files = new CopyOnWriteArrayList<>();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void listen() {
        System.out.println("Listening for connections on port " + this.serverSocket.getLocalPort() + "...");
        try {
            while (true) {
                Socket socket = this.serverSocket.accept();
                new Thread(new ClientThread(this, socket));
                System.out.println("Accepted a connection from " + socket.getInetAddress().toString());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void addFile(File file) {
        this.files.add(file);
    }

    public Iterator<File> getFilesIterator() {
        return this.files.iterator();
    }
}
