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

/**
 * Created by walter on 2017-03-22.
 */
public class FileProviderThread implements Runnable {
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private WJReader reader;
    private WJWriter writer;

    public FileProviderThread(Socket socket) {
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

    @Override
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
                        System.err.println("Non-JSON request from peer");
                        this.close();
                        break loop;
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            this.close();
        }
    }

    private void handleJSONRequest() {
        try {
            String jsonString = this.reader.getJsonString();
            String action = WJMessage.getAction(jsonString);

            switch (action) {
                case "block_list":
                    BlockListRequest blockListRequest = WJMessage.parseBlockListRequest(jsonString);
                    this.handleBlockListRequest(blockListRequest);
                    break;

                case "block":
//                    FileListRequest fileListRequest = WJMessage.parseFileListRequest(jsonString);
                    this.handleBlockRequest();
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

    private void handleBlockListRequest(BlockListRequest blockListRequest) {

    }

    private void handleBlockRequest(){

    }


    private void close() {
    }
}
