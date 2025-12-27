package kr.co.jparangdev.application.common.port;

/**
 * Event publishing abstraction interface (Output Port).
 * Provides a technology-agnostic API for publishing domain events to a message
 * broker.
 * Implementations may use Kafka, RabbitMQ, or any other messaging system.
 *
 * <p>
 * Supports both synchronous and asynchronous publishing modes:
 * </p>
 * <ul>
 * <li>{@link #publish} - Waits for broker acknowledgment (safer, slower)</li>
 * <li>{@link #publishAsync} - Fire-and-forget (faster, less reliable)</li>
 * </ul>
 */
public interface EventPublisher {

    /**
     * Publishes an event synchronously and waits for broker acknowledgment.
     *
     * @param topic the destination topic/queue name
     * @param event the event object to publish (will be serialized)
     */
    void publish(String topic, Object event);

    /**
     * Publishes an event with a partition key for ordered processing.
     * Events with the same key are guaranteed to be processed in order.
     *
     * @param topic the destination topic/queue name
     * @param key   partition key (e.g., entity ID) for ordering
     * @param event the event object to publish
     */
    void publish(String topic, String key, Object event);

    /**
     * Publishes an event asynchronously without waiting for acknowledgment.
     * Use when delivery guarantee is less critical than throughput.
     *
     * @param topic the destination topic/queue name
     * @param event the event object to publish
     */
    void publishAsync(String topic, Object event);

    /**
     * Publishes an event asynchronously with a partition key.
     *
     * @param topic the destination topic/queue name
     * @param key   partition key for ordering
     * @param event the event object to publish
     */
    void publishAsync(String topic, String key, Object event);
}
