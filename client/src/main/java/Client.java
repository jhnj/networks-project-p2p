/**
 * Created by johan on 17/03/17.
 *
 */
import wj.exceptions.WJException;
import wj.json.WJFile;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        try {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Choose port: ");
            int port = Integer.parseInt(consoleReader.readLine());
            System.out.print("Enter tracker ip: ");
            String tracker = consoleReader.readLine();

            InetAddress trackerAddress = InetAddress.getByName(tracker);
            Socket socket = new Socket(trackerAddress, 3004);
            ClientSession session = new ClientSession(socket, port);
            FileHandler fileHandler = new FileHandler(session);
            new Thread(new FileProvider(port, fileHandler)).start();

            while (true) {
                System.out.println("Press A to add Files and D to download files");
                String action = consoleReader.readLine();

                switch (action) {
                    case "D":
                        //Request the file list
                        WJFile[] serverFiles = session.requestFileList();

                        if (serverFiles.length == 0) {
                            System.out.println("No files uploaded yet!");
                            break;
                        }

                        System.out.println("Files:");
                        for (int i = 0; i < serverFiles.length; i++) {
                            System.out.println("  " + i + ". " + serverFiles[i].getName());
                        }

                        //Let the user pick a file
                        System.out.print("Please select a file by its index: ");
                        WJFile file = null;
                        do {
                            try {
                                int fileIndex = Integer.parseInt(consoleReader.readLine());
                                if (fileIndex >= 0 && fileIndex < serverFiles.length) {
                                    file = serverFiles[fileIndex];
                                } else {
                                    throw new NumberFormatException();
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid file index, please try again");
                            }
                        } while (file == null);

                        System.out.println("Enter download location: ");
                        String downloadPath = consoleReader.readLine();
                        File filePath = new File(downloadPath.replaceFirst("^~", System.getProperty("user.home")));

                        //Download!
                        System.out.println("Starting download..");
                        FileDownloader fileDownloader = new FileDownloader(fileHandler, session, port);
                        boolean wasDownloaded = fileDownloader.downloadFile(file, filePath);
                        if (wasDownloaded) {
                            System.out.println("File downloaded!");
                        } else {
                            System.out.println("Download failed.");
                        }

                        break;

                    case "A":
                        System.out.println("Enter the file path: ");
                        String path = consoleReader.readLine();
                        System.out.println("Enter the file name: ");
                        String name = consoleReader.readLine();


                        boolean wasAdded = fileHandler.addLocalFile(name, path);
                        if (wasAdded) {
                            System.out.println("File added successfully!");
                        } else {
                            System.out.println("Unable to add the file, perhaps it already exists?");
                        }
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