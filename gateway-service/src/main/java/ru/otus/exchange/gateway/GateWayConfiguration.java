package ru.otus.exchange.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GateWayConfiguration {
    @Bean
    public KeyResolver keyResolver() {
        //return exchange -> Mono.just(exchange.getSession().subscribe().toString());
        //return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
        return exchange -> Mono.just("xmlRouteResolver");
    }
}
