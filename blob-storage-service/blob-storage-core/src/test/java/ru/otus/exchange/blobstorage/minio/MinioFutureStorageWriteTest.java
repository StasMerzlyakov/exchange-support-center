package ru.otus.exchange.blobstorage.minio;

import static ru.otus.exchange.blobstorage.minio.MinioFutureStorage.OBJECT_SHA256_DIGEST;
import static ru.otus.exchange.blobstorage.minio.MinioFutureStorage.OBJECT_SIZE_TAG;

import io.minio.GetObjectTagsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Random;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MinIOContainer;
import ru.otus.exchange.blobstorage.Metadata;
import ru.otus.exchange.blobstorage.StorageData;
import ru.otus.exchange.blobstorage.StorageKey;

class MinioFutureStorageWriteTest {

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

    @SneakyThrows
    @Test
    @DisplayName("write + readMetadata+ delete")
    void test1() {
        String exchange = "exchange";
        String key = "key";
        StorageData storageData = createStorageDataObject();

        StorageKey storageKey = new StorageKey(exchange, key);

        MinioFutureStorage futureStorage = new MinioFutureStorage(minioClient, minioConfig);

        Assertions.assertDoesNotThrow(() -> Assertions.assertTrue(
                futureStorage.writeFuture(storageKey, storageData).get()));

        String objectPath = String.join("/", exchange, key);

        var tags = minioClient.getObjectTags(GetObjectTagsArgs.builder()
                .bucket(minioConfig.bucket())
                .object(objectPath)
                .build());

        Assertions.assertNotNull(tags);

        var size = Integer.parseInt(tags.get().get(OBJECT_SIZE_TAG));
        var sha256Digest = tags.get().get(OBJECT_SHA256_DIGEST);

        Assertions.assertEquals(storageData.metadata().size(), size);
        Assertions.assertEquals(storageData.metadata().sha256Digest(), sha256Digest);

        Assertions.assertDoesNotThrow(() -> {
            var metadata = futureStorage.readMetadataFuture(storageKey).get();
            Assertions.assertEquals(storageData.metadata().size(), metadata.size());
            Assertions.assertEquals(storageData.metadata().sha256Digest(), metadata.sha256Digest());
        });

        Assertions.assertDoesNotThrow(() -> {
            futureStorage.deleteAllFuture(exchange).get();
        });

        var metadata = futureStorage.readMetadataFuture(storageKey).get();
        Assertions.assertNull(metadata);
    }

    @SneakyThrows
    @Test
    @DisplayName("write + readMetadata + delete")
    void test2() {
        String exchange = "exchange";
        String key = "key";
        StorageData storageData = createStorageDataObject();

        StorageKey storageKey = new StorageKey(exchange, key);

        MinioFutureStorage futureStorage = new MinioFutureStorage(minioClient, minioConfig);

        Assertions.assertDoesNotThrow(() -> Assertions.assertTrue(
                futureStorage.writeFuture(storageKey, storageData).get()));

        String objectPath = String.join("/", exchange, key);

        var tags = minioClient.getObjectTags(GetObjectTagsArgs.builder()
                .bucket(minioConfig.bucket())
                .object(objectPath)
                .build());

        Assertions.assertNotNull(tags);

        var size = Integer.parseInt(tags.get().get(OBJECT_SIZE_TAG));
        var sha256Digest = tags.get().get(OBJECT_SHA256_DIGEST);

        Assertions.assertEquals(storageData.metadata().size(), size);
        Assertions.assertEquals(storageData.metadata().sha256Digest(), sha256Digest);

        Assertions.assertDoesNotThrow(() -> {
            var metadata = futureStorage.readMetadataFuture(storageKey).get();
            Assertions.assertEquals(storageData.metadata().size(), metadata.size());
            Assertions.assertEquals(storageData.metadata().sha256Digest(), metadata.sha256Digest());
        });

        Assertions.assertDoesNotThrow(() -> {
            futureStorage.deleteFuture(storageKey).get();
        });

        var metadata = futureStorage.readMetadataFuture(storageKey).get();
        Assertions.assertNull(metadata);
    }

    @SneakyThrows
    private StorageData createStorageDataObject() {
        int size = 2000;
        byte[] byteArray = new byte[size];
        Random random = new Random();
        random.nextBytes(byteArray);

        var byteBuffer = ByteBuffer.allocate(byteArray.length);
        byteBuffer.put(byteArray);
        byteBuffer.flip();
        String sha256Digest = MinioFutureStorage.hexDigest(byteArray);

        return new StorageData(new Metadata(size, sha256Digest), byteBuffer);
    }
}
