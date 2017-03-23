import wj.exceptions.WJException;
import wj.json.WJClient;
import wj.json.WJFile;
import java.io.IOException;

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

        try {
            fileHandler.addRemoteFile(file);
        } catch (IOException e) {
            System.err.println("Unable to add the remote file locally: " + e.getMessage());
        }

        return true;
    }

}
