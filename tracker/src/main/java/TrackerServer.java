import wj.exceptions.FileNotInServerException;
import wj.exceptions.UserNotInFileException;
import wj.json.WJClient;
import wj.json.WJFile;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by walter on 2017-03-18.
 */
public class TrackerServer {
    ServerSocket serverSocket;
    private Map<WJFile, Set<WJClient>> files;

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
                System.out.println("Accepted a connection from " + socket.getInetAddress().toString());
                new Thread(new ClientThread(this, socket)).run();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean addFile(WJFile file) {
        if (this.files.containsKey(file)) {
            return false;
        } else {
            this.files.put(file, Collections.synchronizedSet(new HashSet()));
            return true;
        }
    }

    public void addUserToFile(WJFile file, WJClient user) throws FileNotInServerException {
        if (this.files.containsKey(file)) {
            this.files.get(file).add(user);
        } else {
            throw new FileNotInServerException();
        }
    }

    public void removeUserFromFile(WJFile file, WJClient user) throws FileNotInServerException, UserNotInFileException {
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


    public Set<WJFile> getFiles() {
        return this.files.keySet();
    }
}
