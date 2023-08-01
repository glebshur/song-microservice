package shgo.innowise.trainee.songmicroservice.fileapi.exception;

/**
 * Shows that error occurs during storage process execution.
 */
public class StorageException extends RuntimeException {
    public StorageException() {
        super();
    }

    public StorageException(String message) {
        super(message);
    }
}
