/**
 * Created by johan on 18/03/17.
 */
public class WJException extends Exception {

    public WJException(String s) {
        super(s);
    }

    public WJException() {
        super();
    }

    public WJException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public WJException(Throwable throwable) {
        super(throwable);
    }

    protected WJException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
