package ru.otus.exchange.receiver.info;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import ru.otus.exchange.receiver.conf.ReceiverProperties;
import ru.otus.exchange.receiver.domain.MessageInfo;

@Slf4j
public class JdbcLowLevelStorage implements LowLeverStorage {

    private final JdbcTemplate jdbcTemplate;

    public JdbcLowLevelStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @CacheEvict(value = "acceptable", allEntries = true)
    @Scheduled(fixedRateString = "${receiver.cache-refresh-timeout}")
    public void emptyAcceptableCache() {
        log.info("emptying acceptable cache");
    }

    @Override
    @Cacheable("acceptable")
    public Boolean isAcceptable(String code) {
        return true;
    }

    @Override
    public void insertIgnore(MessageInfo messageInfo) {

    }

    @Override
    public String getProcessGUID(String processGUID, String messageID) {
        return processGUID;
    }

    @Override
    public String findDiscriminator(String code) {
        return "ExchangeMessage";
    }
}
