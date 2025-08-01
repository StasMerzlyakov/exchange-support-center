package ru.otus.exchange.blobstorage;

import java.nio.ByteBuffer;
import reactor.core.publisher.Mono;

public interface Storage {
    Mono<StorageData> read(StorageKey storageKey);

    // true - object created
    // false - same object already exists
    Mono<Boolean> write(StorageKey storageKey, ByteBuffer byteBuffer);

    // true - delete success
    // false - no data found to delete
    Mono<Boolean> delete(StorageKey storageKey);

    // true - delete success
    // false - no data found to delete
    Mono<Boolean> deleteAll(String exchange);

    Mono<Metadata> getMetadata(StorageKey storageKey);
}
