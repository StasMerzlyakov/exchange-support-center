package ru.otus.exchange.receiver.conf.sender;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.exchange.common.SagaMessage;
import ru.otus.exchange.receiver.SagaSender;
import ru.otus.exchange.receiver.errors.SenderException;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "kafkaSenderType", havingValue = "dummy")
public class DummySenderConfiguration {

    @Bean
    public SagaSender dummySender() {
        return message -> log.info("message {} sent success", message);
    }
}
