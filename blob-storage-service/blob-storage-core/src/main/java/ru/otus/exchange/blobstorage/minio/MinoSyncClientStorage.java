package ru.otus.exchange.blobstorage.minio;

import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;
import io.minio.*;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import ru.otus.exchange.blobstorage.Metadata;
import ru.otus.exchange.blobstorage.StorageKey;

@Slf4j
public class MinoSyncClientStorage implements MinioSyncStorage {

    private final MinioClient minioClient;

    private final MinioConfig minioConfig;

    public MinoSyncClientStorage(MinioClient minioClient, MinioConfig minioConfig) {
        this.minioClient = minioClient;
        this.minioConfig = minioConfig;
    }

    @Override
    @SuppressWarnings("java:S1168")
    public List<StorageKey> listExchange(String exchange) {
        try {
            ListObjectsArgs args = ListObjectsArgs.builder()
                    .bucket(minioConfig.bucket())
                    .prefix(exchange)
                    .recursive(true)
                    .includeVersions(true)
                    .build();

            var listObjectsIterator = minioClient.listObjects(args).iterator();
            List<StorageKey> resultList = new ArrayList<>();

            while (listObjectsIterator.hasNext()) {
                var item = listObjectsIterator.next();
                var objectPath = item.get().objectName();
                var storageKey = fromObjectPath(objectPath);
                resultList.add(storageKey);
            }
            return resultList;
        } catch (Exception e) {
            log.error("listExchange error", e);
            return null;
        }
    }

    @Override
    public boolean writeObject(StorageKey storageKey, ByteBuffer byteBuffer) {
        Map<String, String> metadata = new HashMap<>();

        var objectPath = toObjectPath(storageKey);

        var size = byteBuffer.remaining();
        var sha256Digest = hexDigest(byteBuffer.array());

        metadata.put(OBJECT_SIZE_TAG, String.valueOf(size));
        metadata.put(OBJECT_SHA256_DIGEST, sha256Digest);

        try (var is = new ByteBufferBackedInputStream(byteBuffer)) {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(minioConfig.bucket()).object(objectPath).stream(is, size, -1)
                            .tags(metadata)
                            .build());
            return true;
        } catch (Exception e) {
            log.error("write error", e);
            return false;
        }
    }

    @Override
    public Metadata readMetadata(StorageKey storageKey) {
        try {
            var objectPath = toObjectPath(storageKey);
            var tags = minioClient.getObjectTags(GetObjectTagsArgs.builder()
                    .bucket(minioConfig.bucket())
                    .object(objectPath)
                    .build());
            Metadata metadata = null;
            if (tags == null) {
                log.info("object by path {} not found", objectPath);
            } else {
                var size = Integer.parseInt(tags.get().get(OBJECT_SIZE_TAG));
                var sha256Digest = tags.get().get(OBJECT_SHA256_DIGEST);
                metadata = new Metadata(size, sha256Digest);
            }
            return metadata;
        } catch (Exception e) {
            log.error("readMetadata error", e);
            return null;
        }
    }

    @Override
    public ByteBuffer readObject(StorageKey storageKey) {
        try {
            var objectPath = toObjectPath(storageKey);
            ByteBuffer byteBuffer;
            try (InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioConfig.bucket())
                    .object(objectPath)
                    .build())) {
                byte[] bytes = IOUtils.toByteArray(stream);
                byteBuffer = ByteBuffer.allocate(bytes.length);
                byteBuffer.put(bytes);
            }
            return byteBuffer;
        } catch (Exception e) {
            log.error("readObject error", e);
        }
        return null;
    }

    @Override
    public boolean removeObject(StorageKey storageKey) {
        String objectPath;
        objectPath = toObjectPath(storageKey);
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioConfig.bucket())
                    .object(objectPath)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("removeObject error", e);
            return false;
        }
    }

    @Override
    public boolean removeMetadata(StorageKey storageKey) {
        var objectPath = toObjectPath(storageKey);
        try {
            minioClient.deleteObjectTags(DeleteObjectTagsArgs.builder()
                    .bucket(minioConfig.bucket())
                    .object(objectPath)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("removeMetadata error", e);
            return false;
        }
    }

    private String toObjectPath(StorageKey storageKey) {
        return String.join("/", storageKey.exchange(), storageKey.key());
    }

    private StorageKey fromObjectPath(String objectPath) {
        var args = objectPath.split("/");
        return new StorageKey(args[0], args[1]);
    }

    @SneakyThrows
    static String hexDigest(byte[] byteArray) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(byteArray);

        HexFormat hex = HexFormat.of();
        return hex.formatHex(encodedHash);
    }

    static final String OBJECT_SIZE_TAG = "Size";
    static final String OBJECT_SHA256_DIGEST = "Digest";
}
