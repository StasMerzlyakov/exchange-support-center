package ru.otus.exchange.blobstorage.minio;

import java.nio.ByteBuffer;
import java.util.List;
import ru.otus.exchange.blobstorage.Metadata;
import ru.otus.exchange.blobstorage.StorageKey;

public interface MinioSyncStorage {
    List<StorageKey> listExchange(String exchange);

    boolean writeObject(StorageKey storageKey, ByteBuffer byteBuffer);

    Metadata readMetadata(StorageKey storageKey);

    ByteBuffer readObject(StorageKey storageKey);

    boolean removeObject(StorageKey storageKey);

    boolean removeMetadata(StorageKey storageKey);
}
