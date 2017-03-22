import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by walter on 2017-03-22.
 */
public class FileProvider implements Runnable {
    private ServerSocket serverSocket;
    private int port;

    public FileProvider(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("IOException while opening the file provider: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = this.serverSocket.accept();
                new FileProviderThread(socket).run();
            } catch (IOException e) {
                System.out.println("IOException in FileProvider: " + e.getMessage());
            }
        }
    }
}
