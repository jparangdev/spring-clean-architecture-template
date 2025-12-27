package kr.co.jparangdev.transients.cache;

import kr.co.jparangdev.application.common.port.CachePort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

/**
 * Redis implementation of the CachePort interface.
 * Uses Spring Data Redis for cache operations.
 */
@Component
public class RedisCacheAdapter implements CachePort {

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheAdapter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(type.cast(value));
    }

    @Override
    public void put(String key, Object value) {
        put(key, value, DEFAULT_TTL);
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void evictByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public boolean exists(String key) {
        Boolean exists = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(exists);
    }
}
