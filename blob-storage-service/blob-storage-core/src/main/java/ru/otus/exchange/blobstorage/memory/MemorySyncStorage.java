package ru.otus.exchange.blobstorage.memory;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import ru.otus.exchange.blobstorage.Metadata;
import ru.otus.exchange.blobstorage.StorageData;
import ru.otus.exchange.blobstorage.StorageKey;
import ru.otus.exchange.blobstorage.SyncStorage;

public class MemorySyncStorage implements SyncStorage {

    private final Map<StorageKey, Metadata> metadataMap = new ConcurrentHashMap<>();

    private final BufferCache bufferCache;

    public MemorySyncStorage(BufferCache bufferCache) {
        this.bufferCache = bufferCache;
    }

    @Override
    public List<StorageKey> listExchange(String exchange) {
        return metadataMap.keySet().stream()
                .filter(storageKey -> exchange.equals(storageKey.exchange()))
                .toList();
    }

    @Override
    public boolean writeObject(StorageKey storageKey, StorageData storageData) {
        bufferCache.put(storageKey, storageData.byteBuffer(), sKey -> metadataMap.put(sKey, storageData.metadata()));
        return true;
    }

    @Override
    public Metadata readMetadata(StorageKey storageKey) {
        return metadataMap.get(storageKey);
    }

    @Override
    public ByteBuffer readObject(StorageKey storageKey) {
        return bufferCache.get(storageKey, metadataMap::remove);
    }

    @Override
    public boolean removeObject(StorageKey storageKey) {
        bufferCache.remove(storageKey, metadataMap::remove);
        return true;
    }

    @Override
    public boolean removeMetadata(StorageKey storageKey) {
        return true; // not supported
    }
}
