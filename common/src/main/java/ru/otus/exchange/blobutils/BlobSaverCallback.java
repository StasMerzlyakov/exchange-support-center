package ru.otus.exchange.blobutils;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface BlobSaverCallback {
    void saveObject(String exchange, String fileName, byte[] object);
}
