package pw.ollie.commandcodes.storage;

/**
 * Thrown when there is an error writing things to files / reading things from
 * them
 */
public class StorageException extends Exception {
    private static final long serialVersionUID = 5951441031564234865L;

    public StorageException() {
    }

    public StorageException(final String reason) {
        super(reason);
    }

    public StorageException(final Throwable cause) {
        super(cause);
    }

    public StorageException(final String reason, final Throwable cause) {
        super(reason, cause);
    }
}
