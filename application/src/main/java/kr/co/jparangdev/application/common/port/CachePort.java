package kr.co.jparangdev.application.common.port;

import java.time.Duration;
import java.util.Optional;

/**
 * Cache abstraction interface (Output Port).
 * Provides a technology-agnostic API for caching operations.
 * Implementations may use Redis, Memcached, or any other caching solution.
 */
public interface CachePort {

    /**
     * Retrieves a value from the cache.
     *
     * @param key  the cache key
     * @param type the expected return type
     * @param <T>  the type of the cached value
     * @return an Optional containing the cached value, or empty if not found
     */
    <T> Optional<T> get(String key, Class<T> type);

    /**
     * Stores a value in the cache with the default TTL.
     *
     * @param key   the cache key
     * @param value the value to cache
     */
    void put(String key, Object value);

    /**
     * Stores a value in the cache with a specified TTL.
     *
     * @param key   the cache key
     * @param value the value to cache
     * @param ttl   time-to-live duration
     */
    void put(String key, Object value, Duration ttl);

    /**
     * Removes a value from the cache.
     *
     * @param key the cache key to evict
     */
    void evict(String key);

    /**
     * Removes all keys matching the given pattern.
     * Pattern syntax depends on the underlying implementation (e.g., "user:*" for
     * Redis).
     *
     * @param pattern the key pattern to match
     */
    void evictByPattern(String pattern);

    /**
     * Checks if a key exists in the cache.
     *
     * @param key the cache key
     * @return true if the key exists, false otherwise
     */
    boolean exists(String key);
}
