package ru.otus.exchange.blobstorage.grpc.mapping;

import java.nio.ByteBuffer;
import java.util.Random;
import lombok.SneakyThrows;
import ru.otus.exchange.blobstorage.Metadata;
import ru.otus.exchange.blobstorage.Storage;
import ru.otus.exchange.blobstorage.StorageData;

class Utils {
    private Utils() {}

    @SneakyThrows
    public static StorageData createStorageDataObject() {
        int size = 2000;
        byte[] byteArray = new byte[size];
        Random random = new Random();
        random.nextBytes(byteArray);

        var byteBuffer = ByteBuffer.allocate(byteArray.length);
        byteBuffer.put(byteArray);
        byteBuffer.flip();
        String sha256Digest = Storage.hexDigest(byteArray);

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
        String sha256Digest = Storage.hexDigest(byteArray);

        return new Metadata(size, sha256Digest);
    }
}
