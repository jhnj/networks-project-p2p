import wj.exceptions.FileNotInServerException;
import wj.exceptions.UserNotInFileException;
import wj.exceptions.WJException;
import wj.json.*;
import wj.reader.WJReader;
import wj.reader.WJType;
import wj.writer.WJWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import static wj.reader.WJType.JSON_STRING;

public class ClientThread implements Runnable {
    private TrackerServer server;
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private WJReader reader;
    private WJWriter writer;
    private WJClient client;
    private Set<WJFile> files;

    ClientThread(TrackerServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.files = new HashSet();

        try {
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();

            this.reader = new WJReader(this.input);
            this.writer = new WJWriter(this.output);

            WJType resultType = this.reader.getResultType();
            if (resultType != JSON_STRING) {
                System.err.println("Client did not send SetupPortRequest");
            }

            int port = this.getJSONPortNumber();

            OKResponse response = new OKResponse(port != -1);
            String responseString = WJMessage.stringifyOKResponse(response);
            writer.writeJsonString(responseString);

            if (port == -1) {
                System.err.println("Error in SetupPortRequest");
            }

            this.client = new WJClient(this.socket.getInetAddress().getHostAddress(), port);
        } catch (IOException e) {
            System.err.println(e);
            this.close();
        }
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

    private int getJSONPortNumber() {
        try {
            String jsonString = this.reader.getJsonString();
            String action = WJMessage.getAction(jsonString);
            if (action.equals("port")) {
                SetupPortRequest setupPortRequest = WJMessage.parseSetupPortRequest(jsonString);
                return setupPortRequest.getPort();
            }

            return -1;
        } catch (IOException e) {
            System.out.println("IOException while parsing JSON string: " + e.getMessage());
            return -1;
        } catch (WJException e) {
            System.out.println("Error while retrieving JSON string: " + e.getMessage());
            return -1;
        }
    }

    private void handleJSONRequest() {
        try {
            String jsonString = this.reader.getJsonString();
            String action = WJMessage.getAction(jsonString);

            switch (action) {
                case "file_list":
                    FileListRequest fileListRequest = WJMessage.parseFileListRequest(jsonString);
                    this.handleFileListRequest(fileListRequest);
                    break;

                case "add_file":
                    AddFileRequest addFileRequest = WJMessage.parseAddFileRequest(jsonString);
                    this.handleAddFileRequest(addFileRequest);
                    break;

                case "file_clients":
                    FileClientsRequest fileClientsRequest = WJMessage.parseFileClientsRequest(jsonString);
                    this.handleFileClientsRequest(fileClientsRequest);
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

        //Add the file
        WJFile file = request.getFile();
        boolean wasAdded = this.server.addFile(file);

        //Associate the user with the file
        if (wasAdded) {
            try {
                this.server.addClientToFile(file, this.client);
                this.files.add(file);
            } catch (FileNotInServerException e) {
                System.err.println("FileNotInServerException: " + e.getMessage());
            }
        }

        //Respond to the request
        OKResponse response = new OKResponse(wasAdded);
        String responseString = WJMessage.stringifyOKResponse(response);
        writer.writeJsonString(responseString);
    }

    private void handleFileClientsRequest(FileClientsRequest request) throws IOException {
        WJClient[] clients;

        try {
            Set<WJClient> clientsSet = this.server.getClientsWithFile(request.getFile());
            clients = clientsSet.toArray(new WJClient[clientsSet.size()]);
        } catch (FileNotInServerException e) {
            System.err.println("FileNotInServerException requesting clients for " + request.getFile().getName() + ": "
                                + e.getMessage());
            clients = new WJClient[0];
        }

        FileClientsResponse response = new FileClientsResponse(clients);
        String responseString = WJMessage.stringifyFileClientsResponse(response);
        writer.writeJsonString(responseString);
    }

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
        for (WJFile file : this.files) {
            try {
                this.server.removeClientFromFile(file, this.client);
            } catch (FileNotInServerException fileNotInServerException) {
                System.err.println("FileNotInServerException: " + fileNotInServerException);
            } catch (UserNotInFileException userNotInFileException) {
                System.err.println("UserNotInFileException: " + userNotInFileException);
            }
        }
    }

}
