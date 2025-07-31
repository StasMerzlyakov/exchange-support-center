package ru.otus.exchange.blobstorage.exceptions;

import ru.otus.exchange.blobstorage.CommonStorageException;

public class MemoryException extends CommonStorageException {
    public MemoryException(String message) {
        super(message);
    }

    public MemoryException(Throwable throwable) {
        super(throwable);
    }
}
