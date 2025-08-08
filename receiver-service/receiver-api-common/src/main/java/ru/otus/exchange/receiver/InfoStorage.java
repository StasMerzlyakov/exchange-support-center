package ru.otus.exchange.receiver;

import ru.otus.exchange.common.Discriminator;
import ru.otus.exchange.receiver.domain.MessageInfo;
import ru.otus.exchange.receiver.errors.InfoStorageException;

import javax.xml.namespace.QName;
import java.util.UUID;

public interface InfoStorage {
    Boolean isAcceptable(String code) throws InfoStorageException;
    UUID putIfNotExistsAndGetProcessGUID(MessageInfo messageInfo) throws InfoStorageException;
    Discriminator findDiscriminator(QName bodyQName) throws InfoStorageException;
}
