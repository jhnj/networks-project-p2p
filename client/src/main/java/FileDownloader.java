import wj.exceptions.WJException;
import wj.json.*;
import wj.reader.WJReader;
import wj.reader.WJType;
import wj.writer.WJWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by walter on 2017-03-23.
 */
public class FileDownloader {

    public static boolean downloadFile(WJFile file, FileHandler fileHandler, ClientSession session) {
        //Request available peers from the tracker
        WJClient[] clients = null;
        try {
            clients = session.requestFileClients(file);
        } catch (IOException | WJException e) {
            System.err.println("Unable to request file peers: " + e.getMessage());
            return false;
        }

        if (clients.length == 0) {
            System.out.println("No peers available at the moment..");
            return false;
        }

        //Add the file locally
        try {
            fileHandler.addRemoteFile(file);
        } catch (IOException e) {
            System.err.println("Unable to add the remote file locally: " + e.getMessage());
        }

        //Check what blocks the clients have
        Map<WJClient, int[]> clientBlocks = new HashMap();

        for (WJClient client : clients) {
            try (Socket socket = new Socket(client.getInetAddress(), client.getPort())) {
                socket.setSoTimeout(1000); //Timeout read calls after one second
                InputStream in = socket.getInputStream();
                WJReader reader = new WJReader(in);
                OutputStream out = socket.getOutputStream();
                WJWriter writer = new WJWriter(out);

                //Request the block list
                BlockListRequest blockListRequest = new BlockListRequest(file.getHash());
                writer.writeJsonString(WJMessage.stringifyBlockListRequest(blockListRequest));

                //Retrieve the block list
                WJType resultType = reader.getResultType();
                if (resultType != WJType.JSON_STRING) {
                    throw new WJException("Bad result type from client");
                }
                String jsonString = reader.getJsonString();
                BlockListResponse blockListResponse = WJMessage.parseBlockListResponse(jsonString);

                clientBlocks.put(client, blockListResponse.getBlocks());
            } catch (IOException e) {
                System.out.println("IOException when retrieving blocks from client: " + e.getMessage());
                System.out.println("Continuing..");
            } catch (WJException e) {
                System.out.println("WJException when retrieving blocks from client: " + e.getMessage());
                System.out.println("Continuing..");
            }
        }



        return true;
    }

}
