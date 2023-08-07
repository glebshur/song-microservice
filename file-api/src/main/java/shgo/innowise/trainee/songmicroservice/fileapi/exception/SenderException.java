package shgo.innowise.trainee.songmicroservice.fileapi.exception;

/**
 * Shows that error occurs during sending message.
 */
public class SenderException extends RuntimeException {
    public SenderException() {
        super();
    }

    public SenderException(String message) {
        super(message);
    }
}
