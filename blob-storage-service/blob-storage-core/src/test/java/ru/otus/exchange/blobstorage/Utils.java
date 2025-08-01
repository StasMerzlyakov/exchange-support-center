package ru.otus.exchange.blobstorage;

import java.nio.ByteBuffer;
import java.util.Random;
import lombok.SneakyThrows;

public class Utils {
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
}
