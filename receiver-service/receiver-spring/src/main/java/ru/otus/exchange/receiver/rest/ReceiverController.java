package ru.otus.exchange.receiver.rest;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static ru.otus.exchange.common.Constants.REQUEST_ID;

@RestController("/receiver/v1")
@Slf4j
public class ReceiverController implements ReceiverSwaggerApi {

    @PostMapping(value = "/", consumes = "application/xml;charset=utf-8")
    @Override
    public void receive(
            @RequestHeader(REQUEST_ID)
            String requestId,
            @RequestBody
            byte[] message) {
        log.info("requestId {}", requestId);
    }
}
