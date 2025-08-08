package ru.otus.exchange.receiver;

import ru.otus.exchange.common.SagaMessage;
import ru.otus.exchange.receiver.errors.SenderException;

public interface SagaSender {
    void sent(SagaMessage message) throws SenderException;
}
