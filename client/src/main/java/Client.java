/**
 * Created by johan on 17/03/17.
 *
 */
import wj.exceptions.WJException;
import wj.json.AddFileRequest;
import wj.json.FileListRequest;
import wj.json.WJFile;
import wj.json.WJMessage;
import wj.reader.WJReader;
import wj.reader.WJType;
import wj.writer.WJWriter;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;

public class Client {
    Map<WJFile, String> files;

    public static void main(String[] args) {
        try {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Choose port: ");
            int port = Integer.parseInt(consoleReader.readLine());
            System.out.print("Enter tracker ip:");
            String tracker = consoleReader.readLine();

            Socket socket = new Socket(tracker, 3004, InetAddress.getByName("localhost"), port);
            ClientSession session = new ClientSession(socket);

            while (true) {
                System.out.println("Press A to add Files and D to download files");
                String action = consoleReader.readLine();

                switch (action) {
                    case "D":
                        session.downloadFileList();
                        break;

                    case "A":
                        System.out.println("Enter the file path: ");
                        String path = consoleReader.readLine();
                        String[] blocks = { "jaaa", "hmmm" };
                        WJFile file = new WJFile("dummy_file.jpg", 3004, "abc", blocks);

                        session.addFile(file);
                        break;

                    default:
                }
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException: " + e.getMessage());
        } catch (WJException e) {
            System.err.println("WJException: " + e.getMessage());
        }

    }
}