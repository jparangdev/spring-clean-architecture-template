package kr.co.jparangdev.messaging.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.jparangdev.application.common.port.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka implementation of the EventPublisher interface.
 * Publishes domain events to Kafka topics as JSON messages.
 */
@Component
public class KafkaEventPublisherAdapter implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisherAdapter.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaEventPublisherAdapter(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(String topic, Object event) {
        publish(topic, null, event);
    }

    @Override
    public void publish(String topic, String key, Object event) {
        try {
            String payload = serializeEvent(event);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, payload);
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish event to topic {}: {}", topic, ex.getMessage(), ex);
                } else {
                    log.debug("Event published to topic {} with offset {}",
                            topic, result.getRecordMetadata().offset());
                }
            });
            // Wait for completion in sync mode
            future.join();
        } catch (Exception e) {
            log.error("Error publishing event to topic {}: {}", topic, e.getMessage(), e);
            throw new EventPublishException(topic, e);
        }
    }

    @Override
    public void publishAsync(String topic, Object event) {
        publishAsync(topic, null, event);
    }

    @Override
    public void publishAsync(String topic, String key, Object event) {
        try {
            String payload = serializeEvent(event);
            kafkaTemplate.send(topic, key, payload)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish async event to topic {}: {}", topic, ex.getMessage(), ex);
                        } else {
                            log.debug("Async event published to topic {} with offset {}",
                                    topic, result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.error("Error publishing async event to topic {}: {}", topic, e.getMessage(), e);
        }
    }

    private String serializeEvent(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new EventSerializationException(event.getClass().getName(), e);
        }
    }

    /**
     * Exception thrown when event publishing fails.
     */
    public static class EventPublishException extends RuntimeException {
        public EventPublishException(String topic, Throwable cause) {
            super("Failed to publish event to topic: " + topic, cause);
        }
    }

    /**
     * Exception thrown when event serialization fails.
     */
    public static class EventSerializationException extends RuntimeException {
        public EventSerializationException(String eventType, Throwable cause) {
            super("Failed to serialize event of type: " + eventType, cause);
        }
    }
}
