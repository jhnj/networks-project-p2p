package wj.exceptions;

/**
 * Created by johan on 21/03/17.
 */
public class InvalidHashException extends Throwable {
    public InvalidHashException() {
    }

    public InvalidHashException(String s) {
        super(s);
    }

    public InvalidHashException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidHashException(Throwable throwable) {
        super(throwable);
    }

    public InvalidHashException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
