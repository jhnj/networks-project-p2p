import wj.exceptions.WJException;
import wj.json.AddFileRequest;
import wj.json.FileListRequest;
import wj.json.WJFile;
import wj.json.WJMessage;
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

    public void addFile(WJFile file) throws IOException {
        AddFileRequest addFileRequest = new AddFileRequest(file);
        writer.writeJsonString(WJMessage.stringifyAddFileRequest(addFileRequest));
    }

    public void downloadFileList() throws IOException, WJException {
        String[] files = { "Test" };
        FileListRequest request = new FileListRequest(files);
        writer.writeJsonString(WJMessage.stringifyFileListRequest(request));

        WJType resultType = reader.getResultType();
        if (resultType == WJType.JSON_STRING) {
            String jsonString = reader.getJsonString();

        } else {
            System.err.println("Invalid response type from server");
        }
    }
}
