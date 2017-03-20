import wj.exceptions.WJException;
import wj.json.*;
import wj.reader.WJReader;
import wj.reader.WJType;
import wj.writer.WJWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Set;

public class ClientThread implements Runnable {
    private TrackerServer server;
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private WJReader reader;
    private WJWriter writer;

    ClientThread(TrackerServer server, Socket socket) {
        this.server = server;
        this.socket = socket;

        try {
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();
        } catch (IOException e) {
            System.err.println(e);
            this.close();
        }

        this.reader = new WJReader(this.input);
        this.writer = new WJWriter(this.output);
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
            String action = WJMessage.getAction(jsonString);

            switch (action) {
                case "file_list":
                    FileListRequest fileListRequest = WJMessage.parseFileListRequest(jsonString);
                    handleFileListRequest(fileListRequest);
                    break;

                case "add_file":
                    AddFileRequest addFileRequest = WJMessage.parseAddFileRequest(jsonString);
                    handleAddFileRequest(addFileRequest);
                    break;

                default:
                    System.out.println("Unknown action type " + action + ", skipping");
                    break;
            }
        } catch (IOException e) {
            System.out.println("IOException while parsing JSON string: " + e.getMessage());
        } catch (WJException e) {
            System.out.println("Error while retrieving JSON string: " + e.getMessage());
        }
    }

    private void handleAddFileRequest(AddFileRequest request) throws IOException {
        System.out.println("File add request from: " + this.socket.getInetAddress().getHostName());
        WJFile file = request.getFile();
        boolean wasAdded = this.server.addFile(file);
        AddFileResponse response = new AddFileResponse(wasAdded);
        String responseString = WJMessage.stringifyAddFileResponse(response);
        writer.writeJsonString(responseString);
    }

    //TODO: Check what files the user has
    private void handleFileListRequest(FileListRequest request) throws IOException {
        Set<WJFile> fileSet = server.getFiles();
        WJFile[] files = new WJFile[fileSet.size()];
        files = fileSet.toArray(files);
        FileListResponse response = new FileListResponse(files);
        String responseString = WJMessage.stringifyFileListResponse(response);
        this.writer.writeJsonString(responseString);
        System.out.println("File list request from: " + this.socket.getInetAddress().getHostName());
    }

    /** Closes the connection and removes the user from any files it is associated to.
     *
     *  Called when the pipe is broken.
     */
    private void close() {

        //TODO
    }

}
