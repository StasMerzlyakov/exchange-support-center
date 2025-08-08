package ru.otus.exchange.common;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Discriminator {

    ExchangeMessage("exchangeMessage");

    @JsonValue
    private final String value;

    Discriminator(String value) {
        this.value = value;
    }
}
