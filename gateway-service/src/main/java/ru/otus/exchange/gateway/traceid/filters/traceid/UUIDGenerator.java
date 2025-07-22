package ru.otus.exchange.gateway.traceid.filters.traceid;

import java.util.UUID;

public interface UUIDGenerator {
    UUID next();
}
