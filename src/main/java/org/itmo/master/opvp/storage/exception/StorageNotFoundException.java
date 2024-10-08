package org.itmo.master.opvp.storage.exception;

public class StorageNotFoundException extends RuntimeException {

    public StorageNotFoundException(String message) {
        super(message);
    }

    public StorageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageNotFoundException(Throwable cause) {
        super(cause);
    }
}