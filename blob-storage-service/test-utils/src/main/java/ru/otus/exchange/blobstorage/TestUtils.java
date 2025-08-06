package ru.otus.exchange.blobstorage;

import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.util.Random;

public class TestUtils {
    private TestUtils() {
    }

    @SneakyThrows
    public static StorageData createObject() {
        int size = 2000;
        byte[] byteArray = new byte[size];
        Random random = new Random();
        random.nextBytes(byteArray);

        String sha256Digest = Utils.hexDigest(byteArray);

        return new StorageData(new Metadata(size, sha256Digest), ByteBuffer.wrap(byteArray));
    }

    public static Metadata createMetadata() {
        int size = 2000;
        byte[] byteArray = new byte[size];
        Random random = new Random();
        random.nextBytes(byteArray);

        String sha256Digest = Utils.hexDigest(byteArray);
        return new Metadata(size, sha256Digest);
    }
}
