package ru.otus.exchange.blobstorage;

import java.nio.ByteBuffer;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class ChainStorage implements Storage {

    private final Storage current;
    public final Storage next;

    public ChainStorage(Storage current, Storage next) {
        this.current = current;
        this.next = next;
    }

    @Override
    public Mono<StorageData> read(StorageKey storageKey) {
        return current.read(storageKey)
                .switchIfEmpty(Mono.defer(() -> next.read(storageKey)
                        .doOnNext(storageData -> current.write(storageKey, storageData.byteBuffer()))))
                .doOnError(Mono::error);
    }

    @Override
    public Mono<Boolean> write(StorageKey storageKey, ByteBuffer byteBuffer) {
        return next.write(storageKey, byteBuffer).doOnSuccess(result -> current.write(storageKey, byteBuffer));
    }

    @Override
    public Mono<Boolean> delete(StorageKey storageKey) {
        return current.delete(storageKey).doOnSuccess(b -> next.delete(storageKey));
    }

    @Override
    public Mono<Boolean> deleteAll(String exchange) {
        return current.deleteAll(exchange).doOnSuccess(b -> next.deleteAll(exchange));
    }

    @Override
    public Mono<Metadata> getMetadata(StorageKey storageKey) {
        return current.getMetadata(storageKey)
                .switchIfEmpty(Mono.defer(() -> next.read(storageKey)
                        .doOnNext(storageData -> current.write(storageKey, storageData.byteBuffer()))
                        .map(StorageData::metadata)))
                .doOnError(Mono::error);
    }
}
