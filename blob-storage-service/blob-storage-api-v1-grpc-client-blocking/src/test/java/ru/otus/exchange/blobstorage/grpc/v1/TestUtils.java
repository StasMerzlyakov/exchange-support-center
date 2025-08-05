package ru.otus.exchange.blobstorage.grpc.v1;

import lombok.SneakyThrows;
import ru.otus.exchange.blobstorage.Metadata;
import ru.otus.exchange.blobstorage.StorageData;
import ru.otus.exchange.blobstorage.Utils;

import java.nio.ByteBuffer;
import java.util.Random;

class TestUtils {
    private TestUtils() {}

    @SneakyThrows
    public static StorageData createObject() {
        int size = 2000;
        byte[] byteArray = new byte[size];
        Random random = new Random();
        random.nextBytes(byteArray);

        String sha256Digest = Utils.hexDigest(byteArray);

        return new StorageData(new Metadata(size, sha256Digest), ByteBuffer.wrap(byteArray));
    }
}
