package ru.otus.exchange.blobstorage.exceptions;

import ru.otus.exchange.blobstorage.CommonStorageException;

public class MinioException extends CommonStorageException {
    public MinioException(String message) {
        super(message);
    }
}
