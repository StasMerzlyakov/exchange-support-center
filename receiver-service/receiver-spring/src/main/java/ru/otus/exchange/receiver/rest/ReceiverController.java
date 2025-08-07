package ru.otus.exchange.receiver.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.exchange.receiver.api.v1.swagger.ReceiverSwaggerApi;

@Component
@Slf4j
public class ReceiverController implements ReceiverSwaggerApi {
    @Override
    public void receive(String requestId, byte[] message) {
        log.info("requestId {}", requestId);
    }
}
