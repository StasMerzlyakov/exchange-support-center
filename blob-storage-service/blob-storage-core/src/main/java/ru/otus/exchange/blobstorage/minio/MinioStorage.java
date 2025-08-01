package ru.otus.exchange.blobstorage.minio;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import ru.otus.exchange.blobstorage.*;

@Slf4j
public class MinioStorage implements Storage {

    private final FutureStorage futureStorage;
    private final MinioConfig minioConfig;

    private final long waitSeconds;

    public MinioStorage(FutureStorage futureStorage, MinioConfig minioConfig) {
        this.futureStorage = futureStorage;
        this.minioConfig = minioConfig;
        this.waitSeconds = minioConfig.opTimeout().getSeconds();
    }

    @Override
    public Mono<StorageData> read(StorageKey storageKey) {
        return Mono.fromCallable(() -> futureStorage.readFuture(storageKey).get(waitSeconds, TimeUnit.SECONDS));
    }

    @Override
    public Mono<Boolean> write(StorageKey storageKey, ByteBuffer byteBuffer) {
        return Mono.fromCallable(
                () -> futureStorage.writeFuture(storageKey, byteBuffer).get(waitSeconds, TimeUnit.SECONDS));
    }

    @Override
    public Mono<Boolean> delete(StorageKey storageKey) {
        return Mono.fromCallable(() -> futureStorage.deleteFuture(storageKey).get(waitSeconds, TimeUnit.SECONDS));
    }

    @Override
    public Mono<Boolean> deleteAll(String exchange) {
        return Mono.fromCallable(() -> futureStorage.deleteAllFuture(exchange).get(waitSeconds, TimeUnit.SECONDS));
    }

    @Override
    public Mono<Metadata> getMetadata(StorageKey storageKey) {
        var seconds = minioConfig.opTimeout().getSeconds();
        return Mono.fromCallable(
                () -> futureStorage.readMetadataFuture(storageKey).get(seconds, TimeUnit.SECONDS));
    }
}
