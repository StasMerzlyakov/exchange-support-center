package ru.otus.exchange.blobstorage;

import java.security.MessageDigest;
import java.util.HexFormat;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

public interface Storage {
    Mono<StorageData> read(StorageKey storageKey);

    // true - object created
    // false - same object already exists
    Mono<Boolean> write(StorageKey storageKey, StorageData storageData);

    // true - delete success
    // false - no data found to delete
    Mono<Boolean> delete(StorageKey storageKey);

    // true - delete success
    // false - no data found to delete
    Mono<Boolean> deleteAll(String exchange);

    Mono<Metadata> getMetadata(StorageKey storageKey);

    @SneakyThrows
    static String hexDigest(byte[] byteArray) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(byteArray);

        HexFormat hex = HexFormat.of();
        return hex.formatHex(encodedHash);
    }
}
