package app.data.presentrecipient;


public class AgeNotExistsException extends RuntimeException {
    public AgeNotExistsException(String message) { super(message); }

    public AgeNotExistsException(Throwable cause) { super(cause); }
}