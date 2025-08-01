package ru.otus.exchange.blobstorage;

import static org.mockito.Mockito.*;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.otus.exchange.blobstorage.minio.MinioConfig;

class FutureStorageImplTest {

    @Test
    @DisplayName("read data")
    void test1() {
        SyncStorage syncStorage = Mockito.mock(SyncStorage.class);

        StorageKey storageKey = new StorageKey("exchange", "key");

        StorageData expectedStorageData = createObject();

        MinioConfig minioConfig = new MinioConfig("", Duration.ofSeconds(5));

        FutureStorageImpl futureStorage = new FutureStorageImpl(minioConfig, syncStorage);

        AtomicReference<StorageData> storageDataRef = new AtomicReference<>();

        when(syncStorage.readObject(storageKey)).thenReturn(expectedStorageData.byteBuffer());
        when(syncStorage.readMetadata(storageKey)).thenReturn(expectedStorageData.metadata());

        Assertions.assertDoesNotThrow(
                () -> storageDataRef.set(futureStorage.readFuture(storageKey).get()));

        StorageData actualStorageData = storageDataRef.get();

        Assertions.assertNotNull(actualStorageData);
        Assertions.assertEquals(expectedStorageData.metadata(), actualStorageData.metadata());

        Assertions.assertEquals(actualStorageData.byteBuffer(), expectedStorageData.byteBuffer());
    }

    @Test
    @DisplayName("write data")
    void test2() {
        SyncStorage syncStorage = Mockito.mock(SyncStorage.class);

        StorageKey storageKey = new StorageKey("exchange", "key");

        StorageData expectedStorageData = createObject();

        MinioConfig minioConfig = new MinioConfig("", Duration.ofSeconds(5));

        FutureStorageImpl futureStorage = new FutureStorageImpl(minioConfig, syncStorage);
        when(syncStorage.writeObject(storageKey, expectedStorageData)).thenReturn(true);

        AtomicReference<Boolean> resultRef = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> resultRef.set(
                futureStorage.writeFuture(storageKey, expectedStorageData).get()));
        Assertions.assertTrue(resultRef.get());

        verify(syncStorage, times(1)).writeObject(storageKey, expectedStorageData);
    }

    @Test
    @DisplayName("delete data")
    void test3() {
        SyncStorage syncStorage = Mockito.mock(SyncStorage.class);

        StorageKey storageKey = new StorageKey("exchange", "key");

        MinioConfig minioConfig = new MinioConfig("", Duration.ofSeconds(5));

        FutureStorageImpl futureStorage = new FutureStorageImpl(minioConfig, syncStorage);

        List<StorageKey> storageKeyList = new ArrayList<>();
        storageKeyList.add(storageKey);
        storageKeyList.add(storageKey);
        when(syncStorage.listExchange(storageKey.exchange())).thenReturn(storageKeyList);

        when(syncStorage.removeObject(storageKey)).thenReturn(true);
        when(syncStorage.removeMetadata(storageKey)).thenReturn(true);

        AtomicReference<Boolean> resultRef = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> resultRef.set(
                futureStorage.deleteAllFuture(storageKey.exchange()).get()));
        Assertions.assertTrue(resultRef.get());

        verify(syncStorage, times(1)).listExchange(storageKey.exchange());
        verify(syncStorage, times(2)).removeMetadata(storageKey);
        verify(syncStorage, times(2)).removeObject(storageKey);
    }

    @SneakyThrows
    private StorageData createObject() {
        int size = 2000;
        byte[] byteArray = new byte[size];
        Random random = new Random();
        random.nextBytes(byteArray);

        String sha256Digest = Storage.hexDigest(byteArray);

        return new StorageData(new Metadata(size, sha256Digest), ByteBuffer.wrap(byteArray));
    }
}
