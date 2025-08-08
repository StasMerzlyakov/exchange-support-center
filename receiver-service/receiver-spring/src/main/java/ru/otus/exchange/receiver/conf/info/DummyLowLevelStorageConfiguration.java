package ru.otus.exchange.receiver.conf.info;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.exchange.blobstorage.Metadata;
import ru.otus.exchange.blobstorage.StorageData;
import ru.otus.exchange.blobstorage.StorageKey;
import ru.otus.exchange.blobstorage.StorageSync;
import ru.otus.exchange.receiver.domain.MessageInfo;
import ru.otus.exchange.receiver.info.LowLeverStorage;

@Configuration
@ConditionalOnProperty(name = "infoStorageType", havingValue = "dummy")
public class DummyLowLevelStorageConfiguration {

    @Bean
    public LowLeverStorage dummyStorage() {
        return new LowLeverStorage() {
            @Override
            public Boolean isAcceptable(String code) {
                return null;
            }

            @Override
            public void insertIgnore(MessageInfo messageInfo) {

            }

            @Override
            public String getProcessGUID(String messageID, String processGUID) {
                return processGUID;
            }

            @Override
            public String findDiscriminator(String code) {
                return "";
            }
        };
    }
}
