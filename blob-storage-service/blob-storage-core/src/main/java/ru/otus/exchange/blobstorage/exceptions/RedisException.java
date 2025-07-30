package ru.otus.exchange.blobstorage.exceptions;

import ru.otus.exchange.blobstorage.CommonStorageException;

public class RedisException extends CommonStorageException {
    public RedisException(String message) {
        super(message);
    }
}
