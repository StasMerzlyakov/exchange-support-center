package ru.otus.exchange.receiver.conf.sender;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.otus.exchange.common.SagaMessage;
import ru.otus.exchange.receiver.SagaSender;
import ru.otus.exchange.receiver.conf.ReceiverProperties;
import ru.otus.exchange.receiver.kafka.KafkaSagaSender;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.UUID;
import java.util.Map;

@Configuration
@ConditionalOnProperty(name = "kafkaSenderType", havingValue = "kafka")
public class KafkaSenderConfiguration {

    @Bean
    public ObjectMapper kafkaObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }


    @Bean
    public ProducerFactory<UUID, SagaMessage> producerFactory(
            KafkaProperties kafkaProperties, ObjectMapper kafkaObjectMapper) {
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties();
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        var uuidSerializer = new UUIDSerializer();
        var messageJsonSerializer = new JsonSerializer<SagaMessage>(kafkaObjectMapper);
        return new DefaultKafkaProducerFactory(properties, uuidSerializer, messageJsonSerializer
        );
    }


    @Bean
    public KafkaTemplate<UUID, SagaMessage> kafkaTemplate(ProducerFactory<UUID, SagaMessage> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public SagaSender sagaSender(ReceiverProperties receiverProperties,
                                 KafkaTemplate<UUID, SagaMessage> kafkaTemplate) {
        return new KafkaSagaSender(receiverProperties, kafkaTemplate);
    }

}
