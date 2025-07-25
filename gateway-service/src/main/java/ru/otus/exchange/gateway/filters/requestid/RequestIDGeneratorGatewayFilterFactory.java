package ru.otus.exchange.gateway.filters.requestid;

import io.micrometer.tracing.SpanName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import ru.otus.exchange.common.Constants;

@Component
@Slf4j
public class RequestIDGeneratorGatewayFilterFactory
        extends AbstractGatewayFilterFactory<RequestIDGeneratorGatewayFilterFactory.Config> {

    private final UUIDGenerator uuidGenerator;

    public RequestIDGeneratorGatewayFilterFactory(@Qualifier("uuidGenerator") UUIDGenerator uuidGenerator) {
        super(Config.class);
        this.uuidGenerator = uuidGenerator;
    }

    @SpanName("requestId")
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> chain.filter(exchange.mutate()
                        .request(builder -> builder.header(
                                        Constants.REQUEST_ID,
                                        uuidGenerator.next().toString())
                                .header(Constants.SERVICE_ID, config.serviceId))
                        .build())
                .doOnError(error -> log.error("exception:", error));
    }

    @Data
    public static class Config {
        private String serviceId;
    }
}
