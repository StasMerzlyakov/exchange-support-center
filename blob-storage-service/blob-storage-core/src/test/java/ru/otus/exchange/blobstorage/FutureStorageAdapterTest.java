package ru.otus.exchange.blobstorage;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.concurrent.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FutureStorageAdapterTest {

    @SneakyThrows
    @Test
    void test1() {
        FutureStorage futureStorage = Mockito.mock(FutureStorage.class);
        when(futureStorage.readMetadataFuture(any())).thenReturn(CompletableFuture.completedFuture(null));

        FutureStorageAdapter adapter = new FutureStorageAdapter(Duration.ofSeconds(4), futureStorage);
        StorageKey storageKey = new StorageKey("exchange", "key");
        Assertions.assertFalse(adapter.getMetadata(storageKey).hasElement().block());
    }
}
