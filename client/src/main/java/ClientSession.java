import wj.exceptions.WJException;
import wj.json.*;
import wj.reader.WJReader;
import wj.reader.WJType;
import wj.writer.WJWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by walter on 2017-03-20.
 */
public class ClientSession {
    private Socket socket;
    private InputStream in;
    private WJReader reader;
    private OutputStream out;
    private WJWriter writer;

    public ClientSession(Socket socket) {
        this.socket = socket;
        try {
            in = socket.getInputStream();
            reader = new WJReader(in);
            out = socket.getOutputStream();
            writer = new WJWriter(out);
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException: " + e.getMessage());
        }
    }

    public boolean addFile(WJFile file) throws IOException, WJException {
        AddFileRequest addFileRequest = new AddFileRequest(file);
        writer.writeJsonString(WJMessage.stringifyAddFileRequest(addFileRequest));

        WJType resultType = reader.getResultType();
        if (resultType == WJType.JSON_STRING) {
            String jsonString = reader.getJsonString();
            AddFileResponse response = WJMessage.parseAddFileResponse(jsonString);
            return response.isOk();
        } else {
            System.err.println("Invalid response type from server");
            return false;
        }
    }

    public WJClient[] requestFileClients(WJFile file) throws IOException, WJException {
        FileClientsRequest request = new FileClientsRequest(file);
        writer.writeJsonString(WJMessage.stringifyFileClientsRequest(request));

        WJType resultType = reader.getResultType();
        if (resultType == WJType.JSON_STRING) {
            String jsonString = reader.getJsonString();
            FileClientsResponse response = WJMessage.parseFileClientsResponse(jsonString);
            return response.getClients();
        } else {
            System.err.println("Invalid response type from server");
            WJClient[] noClients = {};
            return noClients;
        }
    }

    public WJFile[] requestFileList() throws IOException, WJException {
        WJFile[] files = {};
        FileListRequest request = new FileListRequest(files);
        writer.writeJsonString(WJMessage.stringifyFileListRequest(request));

        WJType resultType = reader.getResultType();
        if (resultType == WJType.JSON_STRING) {
            String jsonString = reader.getJsonString();
            FileListResponse fileListResponse = WJMessage.parseFileListResponse(jsonString);
            return fileListResponse.getFiles();
        } else {
            System.err.println("Invalid response type from server");
            WJFile[] noFiles = {};
            return noFiles;
        }
    }
}
