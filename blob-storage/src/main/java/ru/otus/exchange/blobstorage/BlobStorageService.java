package ru.otus.exchange.blobstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication(scanBasePackages = "ru.otus.exchange.gateway")
public class BlobStorageService {
    public static void main(String... args) {
        Hooks.enableAutomaticContextPropagation();
        SpringApplication.run(BlobStorageService.class, args);
    }
}
