package ru.otus.exchange.receiver;

import ru.otus.exchange.receiver.domain.MessageInfo;
import ru.otus.exchange.receiver.errors.ContentStorageException;

import java.util.UUID;

public interface ContentStorage {
    void storeMessage(UUID processGUID, MessageInfo messageInfo, byte[] content)  throws ContentStorageException;
}
