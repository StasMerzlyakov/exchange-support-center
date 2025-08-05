package ru.otus.exchange.blobstorage.grpc.mapping;

import java.nio.ByteBuffer;
import java.util.Random;
import lombok.SneakyThrows;
import ru.otus.exchange.blobstorage.Metadata;
import ru.otus.exchange.blobstorage.StorageData;
import ru.otus.exchange.blobstorage.Utils;

class TestUtils {
    private TestUtils() {}

    @SneakyThrows
    public static StorageData createStorageDataObject() {
        int size = 2000;
        byte[] byteArray = new byte[size];
        Random random = new Random();
        random.nextBytes(byteArray);

        var byteBuffer = ByteBuffer.allocate(byteArray.length);
        byteBuffer.put(byteArray);
        byteBuffer.flip();
        String sha256Digest = Utils.hexDigest(byteArray);

        return new StorageData(new Metadata(size, sha256Digest), byteBuffer);
    }

    @SneakyThrows
    public static Metadata createMetadata() {
        int size = 2000;
        byte[] byteArray = new byte[size];
        Random random = new Random();
        random.nextBytes(byteArray);

        var byteBuffer = ByteBuffer.allocate(byteArray.length);
        byteBuffer.put(byteArray);
        byteBuffer.flip();
        String sha256Digest = Utils.hexDigest(byteArray);

        return new Metadata(size, sha256Digest);
    }
}
