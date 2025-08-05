package ru.otus.exchange.blobstorage;

import lombok.SneakyThrows;

import java.security.MessageDigest;
import java.util.HexFormat;

public class Utils {
    private Utils(){}

    @SneakyThrows
    public static String hexDigest(byte[] byteArray) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(byteArray);

        HexFormat hex = HexFormat.of();
        return hex.formatHex(encodedHash);
    }
}
