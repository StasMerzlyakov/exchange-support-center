package ru.otus.exchange.blobstorage.exceptions;

import ru.otus.exchange.blobstorage.CommonStorageException;

public class OverrideForbiddenException extends CommonStorageException {
    public OverrideForbiddenException(String message) {
        super(message);
    }

    public OverrideForbiddenException(Throwable throwable) {
        super(throwable);
    }
}
