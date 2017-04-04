import wj.exceptions.BlockException;
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
    private FileHandler fileHandler;

    public FileProviderThread(Socket socket, FileHandler fileHandler) {
        this.socket = socket;
        this.fileHandler = fileHandler;

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
                    BlockRequest blockRequest = WJMessage.parseBlockRequest(jsonString);
                    this.handleBlockRequest(blockRequest);
                    break;

                default:
                    System.out.println("Unknown action type " + action + ", skipping");
                    break;
            }
        } catch (IOException | WJException | BlockException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleBlockListRequest(BlockListRequest blockListRequest) throws IOException {
        System.out.println("BlockList request from :" + this.socket.getInetAddress().getHostName());

        int[] blocks = fileHandler.getBlockList(blockListRequest.getFileHash());
        BlockListResponse response = new BlockListResponse(blocks);
        String responseString = WJMessage.stringifyBlockListResponse(response);
        writer.writeJsonString(responseString);
    }

    private void handleBlockRequest(BlockRequest blockRequest) throws IOException, BlockException {
        System.out.println("Block request from :" + this.socket.getInetAddress().getHostName());

        byte[] block = fileHandler.getBlock(blockRequest.getFileHash(), blockRequest.getBlockIndex());
        writer.writeBinary(block);
    }


    private void close() {
    }
}
