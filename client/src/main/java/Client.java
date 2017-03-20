/**
 * Created by johan on 17/03/17.
 *
 */
import wj.exceptions.WJException;
import wj.json.WJFile;

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
                        WJFile[] serverFiles = session.requestFileList();
                        System.out.println("Files:");
                        for (int i = 0; i < serverFiles.length; i++) {
                            System.out.println("  " + i + ". " + serverFiles[i].getName());
                        }
                        break;

                    case "A":
                        System.out.println("Enter the file path: ");
                        String path = consoleReader.readLine();
                        String[] blocks = { "jaaa", "hmmm" };
                        WJFile file = new WJFile(path, 3004, "abc", blocks);

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