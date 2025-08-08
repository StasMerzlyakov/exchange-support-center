package ru.otus.exchange.receiver.conf.info;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.exchange.blobstorage.Metadata;
import ru.otus.exchange.blobstorage.StorageData;
import ru.otus.exchange.blobstorage.StorageKey;
import ru.otus.exchange.blobstorage.StorageSync;
import ru.otus.exchange.receiver.info.JdbcLowLevelStorage;
import ru.otus.exchange.receiver.info.LowLeverStorage;

@Configuration
@ConditionalOnProperty(name = "infoStorageType", havingValue = "jdbc")
public class JdbcLowLevelStorageConfiguration {

    @Bean
    public LowLeverStorage jdbcStorage(JdbcTemplate jdbcTemplate) {
        return new JdbcLowLevelStorage(jdbcTemplate);
    }
}
