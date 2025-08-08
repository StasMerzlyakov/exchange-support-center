package ru.otus.exchange.receiver.info;

import lombok.extern.slf4j.Slf4j;
import ru.otus.exchange.common.Discriminator;
import ru.otus.exchange.receiver.InfoStorage;
import ru.otus.exchange.receiver.domain.MessageInfo;
import ru.otus.exchange.receiver.errors.InfoStorageException;

import javax.xml.namespace.QName;
import java.util.UUID;

@Slf4j
public class InfoStorageImpl implements InfoStorage {

    private final LowLeverStorage lowLeverStorage;

    public InfoStorageImpl(LowLeverStorage lowLeverStorage) {
        this.lowLeverStorage = lowLeverStorage;
    }

    @Override
    public Boolean isAcceptable(String code) throws InfoStorageException {
        try {
            var result = lowLeverStorage.isAcceptable(code);
            if (result == null) {
                result = false;
            }
            return result;
        } catch (Exception e) {
            log.error("storage error", e);
            throw new InfoStorageException(e);
        }
    }

    @Override
    public UUID putIfNotExistsAndGetProcessGUID(MessageInfo messageInfo) throws InfoStorageException {
        try {
            lowLeverStorage.insertIgnore(messageInfo);
            return UUID.fromString(lowLeverStorage.getProcessGUID(
                    messageInfo.getProcessGUID().toString(),
                    messageInfo.getMessageID()));
        } catch (Exception e) {
            log.error("putIfNotExistsAndGetProcessGUID", e);
            throw new InfoStorageException(e);
        }
    }

    @Override
    public Discriminator findDiscriminator(QName bodyQName) throws InfoStorageException {
        try {
            String bodyType = bodyQName.toString();
            String discriminatorName = lowLeverStorage.findDiscriminator(bodyType);
            Discriminator discriminator = Discriminator.valueOf(discriminatorName);
            return discriminator;
        } catch (Exception e) {
            log.error("findDiscriminator error", e);
            throw new InfoStorageException(e);
        }
    }
}
