package ru.otus.exchange.blobstorage.minio;

import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;
import io.minio.*;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import ru.otus.exchange.blobstorage.*;

@Slf4j
public class MinioFutureStorage implements FutureStorage {

    private final MinioClient minioClient;

    private final MinioConfig minioConfig;

    private final ExecutorService executor;

    private final long waitSecond;

    public MinioFutureStorage(MinioClient minioClient, MinioConfig minioConfig) {
        this.minioClient = minioClient;
        this.minioConfig = minioConfig;
        this.executor = Executors.newCachedThreadPool();
        this.waitSecond = minioConfig.opTimeout().getSeconds();
    }

    @Override
    public Future<StorageData> readFuture(StorageKey storageKey) {
        return executor.submit(() -> {
            String objectPath = getObjectPath(storageKey);

            CountDownLatch downLatch = new CountDownLatch(2);

            Map<String, Object> threadResult = new ConcurrentHashMap<>();

            startReadMetadataThread(objectPath, downLatch, threadResult);
            startReadObjectThread(objectPath, downLatch, threadResult);

            if (!downLatch.await(waitSecond, TimeUnit.SECONDS)) {
                return null;
            }

            ByteBuffer byteBuffer = (ByteBuffer) threadResult.get(OBJECT_KEY);
            Metadata metadata = (Metadata) threadResult.get(META_KEY);
            if (metadata == null || byteBuffer == null) {
                return null;
            }

            return new StorageData(metadata, byteBuffer);
        });
    }

    @Override
    public Future<Metadata> readMetadataFuture(StorageKey storageKey) {
        return executor.submit(() -> {
            String objectPath = getObjectPath(storageKey);

            CountDownLatch downLatch = new CountDownLatch(1);
            Map<String, Object> threadResult = new ConcurrentHashMap<>();
            startReadMetadataThread(objectPath, downLatch, threadResult);

            if (!downLatch.await(waitSecond, TimeUnit.SECONDS)) {
                return null;
            }
            return (Metadata) threadResult.get(META_KEY);
        });
    }

    @Override
    public Future<Boolean> writeFuture(StorageKey storageKey, StorageData storageData) {
        return executor.submit(() -> {
            CountDownLatch downLatch = new CountDownLatch(1);
            Map<String, Object> threadResult = new ConcurrentHashMap<>();
            String objectPath = getObjectPath(storageKey);
            var byteBuffer = storageData.byteBuffer();
            startWriteThread(objectPath, byteBuffer, downLatch, threadResult);
            if (!downLatch.await(waitSecond, TimeUnit.SECONDS)) {
                return null;
            }
            return (Boolean) threadResult.get(WRITE_RESULT_KEY);
        });
    }

    @Override
    public Future<Boolean> deleteFuture(StorageKey storageKey) {
        return executor.submit(() -> {
            CountDownLatch downLatch = new CountDownLatch(1);
            Map<String, Object> threadResult = new ConcurrentHashMap<>();
            String objectPath = getObjectPath(storageKey);
            startDeleteThread(objectPath, downLatch, threadResult, DELETE_RESULT_KEY);
            if (!downLatch.await(waitSecond, TimeUnit.SECONDS)) {
                return null;
            }
            return (Boolean) threadResult.get(DELETE_RESULT_KEY);
        });
    }

    @Override
    public Future<Boolean> deleteAllFuture(String exchange) {
        return executor.submit(() -> {
            CountDownLatch downLatch = new CountDownLatch(1);
            Map<String, Object> threadResult = new ConcurrentHashMap<>();
            startDeleteAllThread(exchange, downLatch, threadResult);
            if (!downLatch.await(waitSecond, TimeUnit.SECONDS)) {
                return null;
            }
            return (Boolean) threadResult.get(DELETE_RESULT_KEY);
        });
    }

    private void startDeleteThread(
            String objectPath, CountDownLatch latch, Map<String, Object> threadResult, String key) {
        new Thread(() -> {
                    try {
                        minioClient.removeObject(RemoveObjectArgs.builder()
                                .bucket(minioConfig.bucket())
                                .object(objectPath)
                                .build());
                        minioClient.deleteObjectTags(DeleteObjectTagsArgs.builder()
                                .bucket(minioConfig.bucket())
                                .build());
                        threadResult.put(key, true);
                    } catch (Exception e) {
                        log.error("delete error", e);
                        threadResult.put(key, false);
                    } finally {
                        latch.countDown();
                    }
                })
                .start();
    }

    private void startDeleteAllThread(String exchange, CountDownLatch latch, Map<String, Object> threadResult) {
        new Thread(() -> {
                    try {
                        ListObjectsArgs args = ListObjectsArgs.builder()
                                .bucket(minioConfig.bucket())
                                .prefix(exchange)
                                .recursive(true)
                                .includeVersions(true)
                                .build();

                        var listObjectsIterator = minioClient.listObjects(args).iterator();
                        var removeList = new LinkedList<String>();

                        while (listObjectsIterator.hasNext()) {
                            var item = listObjectsIterator.next();
                            removeList.add(item.get().objectName());
                        }

                        CountDownLatch downLatch = new CountDownLatch(removeList.size());

                        ListIterator<String> listIterator = removeList.listIterator();

                        while (listIterator.hasNext()) {
                            var index = listIterator.nextIndex();
                            startDeleteThread(
                                    listIterator.next(),
                                    downLatch,
                                    threadResult,
                                    String.join("_", DELETE_RESULT_KEY, String.valueOf(index)));
                        }

                        downLatch.await();
                        threadResult.put(DELETE_ALL_RESULT_KEY, true);
                    } catch (InterruptedException ie) {
                        log.info("deleteAll interrupted");
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        log.error("deleteAll error", e);
                        threadResult.put(DELETE_ALL_RESULT_KEY, false);
                    } finally {
                        latch.countDown();
                    }
                })
                .start();
    }

    private void startWriteThread(
            String objectPath, ByteBuffer byteBuffer, CountDownLatch latch, Map<String, Object> threadResult) {
        new Thread(() -> {
                    Map<String, String> metadata = new HashMap<>();

                    var size = byteBuffer.remaining();
                    var sha256Digest = hexDigest(byteBuffer.array());

                    metadata.put(MinioFutureStorage.OBJECT_SIZE_TAG, String.valueOf(size));
                    metadata.put(MinioFutureStorage.OBJECT_SHA256_DIGEST, sha256Digest);

                    try (var is = new ByteBufferBackedInputStream(byteBuffer)) {
                        minioClient.putObject(
                                PutObjectArgs.builder().bucket(minioConfig.bucket()).object(objectPath).stream(
                                                is, size, -1)
                                        .tags(metadata)
                                        .build());
                        threadResult.put(WRITE_RESULT_KEY, true);
                    } catch (Exception e) {
                        log.error("write error", e);
                        threadResult.put(WRITE_RESULT_KEY, false);
                    } finally {
                        latch.countDown();
                    }
                })
                .start();
    }

    private void startReadMetadataThread(String objectPath, CountDownLatch latch, Map<String, Object> threadResult) {
        new Thread(() -> {
                    try {
                        var tags = minioClient.getObjectTags(GetObjectTagsArgs.builder()
                                .bucket(minioConfig.bucket())
                                .object(objectPath)
                                .build());
                        if (tags == null) {
                            log.info("object by path {} not found", objectPath);
                        } else {
                            var size = Integer.parseInt(tags.get().get(OBJECT_SIZE_TAG));
                            var sha256Digest = tags.get().get(OBJECT_SHA256_DIGEST);
                            threadResult.put(META_KEY, new Metadata(size, sha256Digest));
                        }
                    } catch (Exception e) {
                        log.error("readMetadata error", e);
                    } finally {
                        latch.countDown();
                    }
                })
                .start();
    }

    private void startReadObjectThread(String objectPath, CountDownLatch latch, Map<String, Object> threadResult) {
        new Thread(() -> {
                    try {
                        ByteBuffer byteBuffer;
                        try (InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                                .bucket(minioConfig.bucket())
                                .object(objectPath)
                                .build())) {
                            byte[] bytes = IOUtils.toByteArray(stream);
                            byteBuffer = ByteBuffer.allocate(bytes.length);
                            byteBuffer.put(bytes);
                            threadResult.put(OBJECT_KEY, byteBuffer);
                        }
                    } catch (Exception e) {
                        log.error("readObject error", e);
                    } finally {
                        latch.countDown();
                    }
                })
                .start();
    }

    private String getObjectPath(StorageKey storageKey) {
        return String.join("/", storageKey.exchange(), storageKey.key());
    }

    @SneakyThrows
    static String hexDigest(byte[] byteArray) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(byteArray);

        HexFormat hex = HexFormat.of();
        return hex.formatHex(encodedHash);
    }

    public static final String OBJECT_SIZE_TAG = "Size";
    public static final String OBJECT_SHA256_DIGEST = "Digest";

    private static final String OBJECT_KEY = "object";
    private static final String META_KEY = "metadata";
    private static final String WRITE_RESULT_KEY = "write_result";
    private static final String DELETE_RESULT_KEY = "delete_result";
    private static final String DELETE_ALL_RESULT_KEY = "delete_all_result";
}
