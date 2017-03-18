import wj.exceptions.WJException;
import wj.reader.WJReader;
import wj.reader.WJType;
import wj.json.FileListRequest;
import wj.json.WJMessage;

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
        try {
            loop: while (true) {
                WJType resultType = this.reader.getResultType();

                switch (resultType) {
                    case JSON_STRING:
                        this.handleJSONRequest();
                        break;
                    case INVALID:
                    default:
                        System.err.println("Non-JSON request from client");
                        this.close();
                        break loop;
                }
            }
        } catch (IOException e) {
            System.out.println("Client " + this.socket.getInetAddress() + " disconnected: " + e.getMessage());
            this.close();
        }
    }

    private void handleJSONRequest() {
        try {
            String jsonString = this.reader.getJsonString();
            System.out.println(jsonString);

            System.out.println(WJMessage.getAction(jsonString));
            FileListRequest flRequest = WJMessage.parseFileListRequest(jsonString);
            System.out.println("Test");
        } catch (IOException e) {
            System.out.println("IOException while parsing JSON string: " + e.getMessage());
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
