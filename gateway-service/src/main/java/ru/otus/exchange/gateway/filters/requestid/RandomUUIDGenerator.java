package ru.otus.exchange.gateway.filters.requestid;

import com.fasterxml.uuid.Generators;
import java.util.UUID;

import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("uuidGenerator")
public class RandomUUIDGenerator implements UUIDGenerator {

    @Override
    @Observed(name = "randomNext")
    public UUID next() {
        return Generators.timeBasedEpochRandomGenerator().generate();
    }
}
