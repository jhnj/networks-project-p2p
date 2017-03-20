/**
 * Created by johan on 17/03/17.
 *
 */
import wj.json.FileListRequest;
import wj.json.WJFile;
import wj.json.WJMessage;
import wj.writer.WJWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;

public class Client {
    Map<WJFile, String> files;

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Choose port: ");
            int port = Integer.parseInt(reader.readLine());
            System.out.print("Enter tracker ip:");
            String tracker = reader.readLine();

            Socket socket = new Socket(tracker, 3004, InetAddress.getByName("localhost"), port);
            OutputStream out = socket.getOutputStream();
            WJWriter writer = new WJWriter(out);

            while (true) {
                System.out.println("Press A to add Files and D to download files");
                String action = reader.readLine();

                switch (action) {
                    case "D":
                        String[] files = { "Test" };
                        FileListRequest request = new FileListRequest(files);
                        writer.writeJsonString(WJMessage.stringifyFileListRequest(request));
                        break;

                    case "A":
                    default:
                }
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException: " + e.getMessage());
        }

    }
}