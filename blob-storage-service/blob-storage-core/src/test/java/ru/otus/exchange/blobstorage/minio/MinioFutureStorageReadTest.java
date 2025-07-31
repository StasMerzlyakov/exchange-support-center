package ru.otus.exchange.blobstorage.minio;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.InsufficientDataException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.testcontainers.containers.MinIOContainer;
import ru.otus.exchange.blobstorage.Metadata;
import ru.otus.exchange.blobstorage.StorageData;
import ru.otus.exchange.blobstorage.StorageKey;

class MinioFutureStorageReadTest {

    private static final String ACCESS_KEY = "accessKey";
    private static final String SECRET_KEY = "secretKey";
    private static final String BUCKET = "bucket";

    static MinIOContainer minio = new MinIOContainer("minio/minio:RELEASE.2023-09-04T19-57-37Z")
            .withUserName(ACCESS_KEY)
            .withPassword(SECRET_KEY);

    private static MinioClient minioClient;

    private static MinioConfig minioConfig;

    @SneakyThrows
    @BeforeAll
    static void startContainers() {
        minio.start();

        String endpoint = minio.getS3URL();

        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
        minioClient.makeBucket(new MakeBucketArgs.Builder().bucket(BUCKET).build());

        minioConfig = new MinioConfig(BUCKET, Duration.ofSeconds(5));
    }

    @AfterAll
    static void stopContainers() {
        minio.stop();
    }

    @Test
    @DisplayName("data exists")
    void test1() {
        String exchange = "exchange";
        String key = "key";
        Metadata metadata = createObject(exchange, key);
        StorageKey storageKey = new StorageKey(exchange, key);

        MinioFutureStorage futureStorage = new MinioFutureStorage(minioClient, minioConfig);

        AtomicReference<StorageData> storageDataRef = new AtomicReference<>();
        Assertions.assertDoesNotThrow(
                () -> storageDataRef.set(futureStorage.readFuture(storageKey).get()));

        StorageData storageData = storageDataRef.get();

        Assertions.assertNotNull(storageData);
        Assertions.assertEquals(metadata, storageData.metadata());

        var byteBuffer = storageData.byteBuffer();
        Assertions.assertNotNull(byteBuffer);
        Assertions.assertTrue(byteBuffer.hasArray());

        String actualDigest = MinioFutureStorage.hexDigest(byteBuffer.array());
        Assertions.assertEquals(metadata.sha256Digest(), actualDigest);

        Assertions.assertEquals(metadata.size(), storageData.metadata().size());
        Assertions.assertEquals(metadata.sha256Digest(), storageData.metadata().sha256Digest());

        Assertions.assertDoesNotThrow(
                () -> futureStorage.deleteFuture(storageKey).get());
    }

    @Test
    @DisplayName("data not exists")
    void test2() {
        String exchange = "exchange";
        String key = "key";
        StorageKey storageKey = new StorageKey(exchange, key);
        MinioFutureStorage futureStorage = new MinioFutureStorage(minioClient, minioConfig);

        AtomicReference<StorageData> storageDataRef = new AtomicReference<>();
        Assertions.assertDoesNotThrow(
                () -> storageDataRef.set(futureStorage.readFuture(storageKey).get()));

        StorageData storageData = storageDataRef.get();

        Assertions.assertNull(storageData);
    }

    @Test
    @DisplayName("assertion thrown")
    @SneakyThrows
    void test3() {
        String exchange = "exchange";
        String key = "key";
        StorageKey storageKey = new StorageKey(exchange, key);

        var mockMinioClient = Mockito.mock(MinioClient.class);

        MinioFutureStorage futureStorage = new MinioFutureStorage(mockMinioClient, minioConfig);

        when(mockMinioClient.getObject(any())).thenThrow(new InsufficientDataException("ex"));

        AtomicReference<StorageData> storageDataRef = new AtomicReference<>();
        Assertions.assertDoesNotThrow(
                () -> storageDataRef.set(futureStorage.readFuture(storageKey).get()));

        StorageData storageData = storageDataRef.get();
        Assertions.assertNull(storageData);
    }

    @SneakyThrows
    private Metadata createObject(String exchange, String key) {
        int size = 2000;
        byte[] byteArray = new byte[size];
        Random random = new Random();
        random.nextBytes(byteArray);

        String sha256Digest = MinioFutureStorage.hexDigest(byteArray);

        Map<String, String> metadata = new HashMap<>();
        metadata.put(MinioFutureStorage.OBJECT_SIZE_TAG, String.valueOf(size));
        metadata.put(MinioFutureStorage.OBJECT_SHA256_DIGEST, sha256Digest);

        InputStream inputStream = new ByteArrayInputStream(byteArray);

        minioClient.putObject(PutObjectArgs.builder().bucket(BUCKET).object(String.join("/", exchange, key)).stream(
                        inputStream, size, -1)
                .tags(metadata)
                .build());

        return new Metadata(size, sha256Digest);
    }
}
