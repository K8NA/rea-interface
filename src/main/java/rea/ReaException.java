package rea;

public class ReaException extends Exception {

    public ReaException() {
    }

    public ReaException(String message) {
        super(message);
    }

    public ReaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReaException(Throwable cause) {
        super(cause);
    }

    public ReaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
