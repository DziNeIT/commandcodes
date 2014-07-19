package com.github.dzineit.commandcodes.storage;

/**
 * Thrown when there is an error writing things to files / reading things from
 * them
 */
public class StorageException extends Exception {
	private static final long serialVersionUID = 5951441031564234865L;

	public StorageException() {
	}

	public StorageException(String reason) {
		super(reason);
	}

	public StorageException(Throwable cause) {
		super(cause);
	}

	public StorageException(String reason, Throwable cause) {
		super(reason, cause);
	}
}
