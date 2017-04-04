import wj.exceptions.WJException;
import wj.json.*;
import wj.reader.WJReader;
import wj.reader.WJType;
import wj.writer.WJWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

/**
 * Created by walter on 2017-03-23.
 */
public class FileDownloader {
    private FileHandler fileHandler;
    private ClientSession session;

    public FileDownloader(FileHandler fileHandler, ClientSession session) {
        this.fileHandler = fileHandler;
        this.session = session;
    }

    public boolean downloadFile(WJFile file) {
        //Request available peers from the tracker
        WJClient[] clients = null;
        System.out.println("Requesting file peers from the tracker..");
        try {
            clients = session.requestFileClients(file);
        } catch (IOException | WJException e) {
            System.err.println("Unable to request file peers: " + e.getMessage());
            return false;
        }

        if (clients.length == 0) {
            System.out.println("No peers available at the moment.");
            return false;
        }

        //Add the file locally
        try {
            fileHandler.addRemoteFile(file);
        } catch (IOException e) {
            System.err.println("Unable to add the remote file locally: " + e.getMessage());
            return false;
        }

        //Check what blocks the clients have
        System.out.println("Checking what blocks the peers have..");
        Map<Integer, ArrayList<WJClient>> clientBlocks = this.getBlocksWithClients(file, clients);

        //Add the clients to a priority queue so that the blocks with the fewest clients come first
        PriorityQueue<BlockClients> blockClientsPQ = new PriorityQueue();
        for (Map.Entry<Integer, ArrayList<WJClient>> entry : clientBlocks.entrySet()) {
            blockClientsPQ.add(new BlockClients(entry.getKey(), entry.getValue()));
        }

        //Download the blocks
        while (!blockClientsPQ.isEmpty()) {
            BlockClients blockClient = blockClientsPQ.poll();
            Boolean blockDownloaded = false;

            for (WJClient client : blockClient.clients) {
                try {
                    //Try downloading the block from a client
                    byte[] data = this.downloadBlockFromClient(file.getHash(), blockClient.block, client);

                    //Store the block
                    fileHandler.storeBlock(file.getHash(), blockClient.block, data);
                    blockDownloaded = true;
                    break;
                } catch (IOException | WJException e) {
                    System.out.println("Unable to download block " + blockClient.block + " from client " + client.getIp()
                                        + ", trying the next client");
                }
            }

            if (blockDownloaded) {
                System.out.println("Downloaded block " + blockClient.block);
            } else {
                System.out.println("Unable to download block " + blockClient.block + ", skipping");
            }
        }

        return true;
    }

    private byte[] downloadBlockFromClient(String fileHash, Integer block, WJClient client) throws IOException, WJException {
        Socket socket = new Socket(client.getInetAddress(), client.getPort());
        WJWriter writer = new WJWriter(socket.getOutputStream());
        WJReader reader = new WJReader(socket.getInputStream());

        BlockRequest blockRequest = new BlockRequest(fileHash, block);
        writer.writeJsonString(WJMessage.stringifyBlockRequest(blockRequest));

        WJType type = reader.getResultType();

        if (type != WJType.BINARY) {
            throw new WJException("Bad response type from server for block from client request: " + type);
        }

        byte[] result = reader.getBinary();

        socket.close();

        return result;
    }

    private class BlockClients implements Comparable<BlockClients> {
        public Integer block;
        public ArrayList<WJClient> clients;

        public BlockClients(Integer block, ArrayList<WJClient> clients) {
            this.block = block;
            this.clients = clients;
        }

        @Override
        public int compareTo(BlockClients o) {
            return this.clients.size() - o.clients.size();
        }
    }

    /** Returns a map pointing from block indices to clients that have that block
     *
     *  If a block is unavailable, the key is not set
     */
    private Map<Integer, ArrayList<WJClient>> getBlocksWithClients(WJFile file, WJClient[] clients) {
        Map<Integer, ArrayList<WJClient>> clientBlocks = new HashMap(); //Block index -> clients

        for (WJClient client : clients) {
            try (Socket socket = new Socket(client.getInetAddress(), client.getPort())) {
//                socket.setSoTimeout(1000000); //Timeout read calls after one second
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

                for (Integer blockIndex : blockListResponse.getBlocks()) {
                    if (!clientBlocks.containsKey(blockIndex)) {
                        clientBlocks.put(blockIndex, new ArrayList());
                    }
                    clientBlocks.get(blockIndex).add(client);
                }
            } catch (IOException e) {
                System.out.println("IOException when retrieving blocks from client: " + e.getMessage());
                System.out.println("Continuing..");
            } catch (WJException e) {
                System.out.println("WJException when retrieving blocks from client: " + e.getMessage());
                System.out.println("Continuing..");
            }
        }

        return clientBlocks;
    }

}
