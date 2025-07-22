package ru.otus.exchange.gateway.traceid.filters.traceid;

import com.fasterxml.uuid.Generators;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Qualifier("uuidGenerator")
public class RandomUUIDGenerator implements UUIDGenerator {

    @Override
    public UUID next() {
        return Generators.timeBasedEpochRandomGenerator().generate();
    }
}
