package org.itmo.master.opvp.storage.exception;

public class StorageOverflowException extends RuntimeException {

    public StorageOverflowException() {
        super("Storage is overflow. Unable to add more items.");
    }

    public StorageOverflowException(String message) {
        super(message);
    }

    public StorageOverflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageOverflowException(Throwable cause) {
        super(cause);
    }
}