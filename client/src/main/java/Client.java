/**
 * Created by johan on 17/03/17.
 *
 */
import wj.json.WJFile;

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

            while (true) {
                System.out.println("Press A to add Files and D to download files");
                String action = reader.readLine();

                switch (action) {
                    case "A":
                        OutputStream out = socket.getOutputStream();
                        String jsonString = "{ \"action\": \"file_list\", \"existing_files\": [\"Test\"] }";
                        byte[] data = new byte[3 + jsonString.length()];
                        data[0] = (byte) 0x00;
                        data[1] = (byte) 0x00;
                        data[2] = (byte) jsonString.length();
                        int i = 3;
                        for (char ch : jsonString.toCharArray()) {
                            data[i] = (byte) ch;
                            i++;
                        }

                        out.write(data);
                        out.flush();
                        break;
                    case "D":
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