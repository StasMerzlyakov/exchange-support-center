package ru.otus.exchange.receiver.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Data
@Configuration
@ConfigurationProperties(prefix = "receiver")
public class ReceiverProperties {
    String nextTopic;
    Duration kafkaWaitTimeout;
    String xmlFileName;
    String blobStorageHost;
    int blobStoragePort;
    Duration cacheRefreshTimeout;
}
