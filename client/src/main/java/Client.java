/**
 * Created by johan on 17/03/17.
 *
 */


import org.joda.time.LocalTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        LocalTime currentTime = new LocalTime();
        System.out.println("The current local time is: " + currentTime);

        try {
            Socket socket = new Socket("localhost", 3004);
            OutputStream out = socket.getOutputStream();

            out.write("some data".getBytes());
            out.flush();
            out.close();

        } catch (IOException ioException) {
            System.err.println("IOException: " + ioException.getMessage());
        }

    }
}