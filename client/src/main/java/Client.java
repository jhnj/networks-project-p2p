/**
 * Created by johan on 17/03/17.
 *
 */


import org.joda.time.LocalTime;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) {
        LocalTime currentTime = new LocalTime();
        System.out.println("The current local time is: " + currentTime);

        try {
            Socket socket = new Socket("localhost", 3004);
            OutputStream out = socket.getOutputStream();
            String jsonString = "{ \"action\": \"file_list\", \"existing_files\": [\"Test\"] }";
            byte[] data = new byte[3 + jsonString.length()];
            data[0] = (byte) 0x00;
            data[1] = (byte) 0x00;
            data[2] = (byte) jsonString.length();
            int i = 3;
            for (char ch: jsonString.toCharArray()) {
                data[i] = (byte) ch;
                i++;
            }

            out.write(data);
            out.flush();
            out.close();

        } catch (IOException ioException) {
            System.err.println("IOException: " + ioException.getMessage());
        }

    }
}