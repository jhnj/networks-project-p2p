/**
 * Created by johan on 17/03/17.
 *
 */


import org.joda.time.LocalTime;

public class Client {
    public static void main(String[] args) {
        LocalTime currentTime = new LocalTime();
        System.out.println("The current local time is: " + currentTime);
    }
}