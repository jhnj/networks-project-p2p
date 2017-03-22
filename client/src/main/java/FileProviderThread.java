import java.net.Socket;

/**
 * Created by walter on 2017-03-22.
 */
public class FileProviderThread implements Runnable {
    private Socket socket;

    public FileProviderThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
