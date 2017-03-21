package wj.exceptions;

/**
 * Created by johan on 21/03/17.
 */
public class BlockException extends Throwable {
    public BlockException() {
    }

    public BlockException(String s) {
        super(s);
    }

    public BlockException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BlockException(Throwable throwable) {
        super(throwable);
    }

    public BlockException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
