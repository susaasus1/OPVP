package org.itmo.master.opvp.storage.exception;

public class StorageLoaderException extends RuntimeException {

    public StorageLoaderException(String message) {
        super(message);
    }

    public StorageLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageLoaderException(Throwable cause) {
        super(cause);
    }
}