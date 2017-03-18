//import org.joda.time.LocalTime;


public class Tracker {
    public static void main(String[] args) {
        System.out.println("Tracker");
        TrackerServer server = new TrackerServer(3004);
        server.listen();
    }
}