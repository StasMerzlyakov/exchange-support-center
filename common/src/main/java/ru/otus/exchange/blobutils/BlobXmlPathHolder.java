package ru.otus.exchange.blobutils;

import org.springframework.cache.annotation.Cacheable;

import java.util.Set;

@FunctionalInterface
public interface BlobXmlPathHolder {
    @Cacheable
    Set<String> getBlobPath(String messageType);
}
