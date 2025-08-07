package ru.otus.exchange.receiver.rest;

import static ru.otus.exchange.common.Constants.REQUEST_ID;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;

@Slf4j
@Controller
public class ReceiverService implements ReceiverSwaggerApi {

    @Override
    public void receive(@RequestHeader(REQUEST_ID) String requestId, @RequestBody byte[] message) {
        log.info("requestId {}", requestId);
    }
}
