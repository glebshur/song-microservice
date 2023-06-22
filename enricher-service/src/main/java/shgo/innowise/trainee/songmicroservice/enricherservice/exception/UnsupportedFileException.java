package shgo.innowise.trainee.songmicroservice.enricherservice.exception;

/**
 * Exception shows that file format is unsupported.
 */
public class UnsupportedFileException extends RuntimeException {
    public UnsupportedFileException(String message) {
        super(message);
    }
}
