import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by walter on 2017-03-18.
 */
public class TrackerServer {
    ServerSocket serverSocket;
    private Map<File, Set<User>> files;

    public TrackerServer(int port) {
        this.files = Collections.synchronizedMap(new HashMap());
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
        this.files.put(file, Collections.synchronizedSet(new HashSet()));
    }

    public void addUserToFile(File file, User user) throws FileNotInServerException {
        if (this.files.containsKey(file)) {
            this.files.get(file).add(user);
        } else {
            throw new FileNotInServerException();
        }
    }

    public void removeUserFromFile(File file, User user) throws FileNotInServerException, UserNotInFileException {
        if (this.files.containsKey(file)) {
            if (this.files.get(file).contains(user)) {
                this.files.get(file).remove(user);
            } else {
                throw new UserNotInFileException();
            }
        } else {
            throw new FileNotInServerException();
        }
    }


    public Set<File> getFiles() {
        return this.files.keySet();
    }
}
