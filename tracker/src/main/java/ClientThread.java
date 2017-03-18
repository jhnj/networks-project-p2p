import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientThread implements Runnable {
    private TrackerServer server;
    private Socket socket;
    private InputStream input;
    private WJReader reader;

    ClientThread(TrackerServer server, Socket socket) {
        this.server = server;
        this.socket = socket;

        try {
            this.input = socket.getInputStream();
        } catch (IOException e) {
            System.err.println(e);
            this.close();
        }

        this.reader = new WJReader(this.input);
    }

    public void run() {
        while (true) {
            WJType resultType = this.reader.getResultType();

            switch (resultType) {
                case JSON_STRING:
                    this.handleJSONRequest();
                default:
                    System.err.println("Non-JSON request from client");
                    break;
            }
        }
    }

    private void handleJSONRequest() {
        try {
            String jsonString = this.reader.getJsonString();
            System.out.println(jsonString);
        } catch (WJException e) {
            System.out.println("Error while retrieving JSON string: " + e.getMessage());
        }
    }

    /** Closes the connection and removes the user from any files it is associated to.
     *
     *  Called when the pipe is broken.
     */
    private void close() {
        //TODO
    }

}
