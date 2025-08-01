package ru.otus.exchange.blobstorage;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

public interface FutureStorage {
    Future<StorageData> readFuture(StorageKey storageKey);

    Future<Metadata> readMetadataFuture(StorageKey storageKey);

    Future<Boolean> writeFuture(StorageKey storageKey, ByteBuffer byteBuffer);

    Future<Boolean> deleteFuture(StorageKey storageKey);

    Future<Boolean> deleteAllFuture(String exchange);
}
