package kr.co.jparangdev.transients.lock;

import kr.co.jparangdev.application.common.port.DistributedLockPort;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Redisson implementation of the DistributedLockPort interface.
 * Provides distributed locking capabilities using Redis.
 */
@Component
public class RedisDistributedLockAdapter implements DistributedLockPort {

    private static final String LOCK_PREFIX = "lock:";

    private final RedissonClient redissonClient;

    public RedisDistributedLockAdapter(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean tryLock(String lockKey, Duration waitTime, Duration leaseTime) {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + lockKey);
        try {
            return lock.tryLock(waitTime.toMillis(), leaseTime.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    @Override
    public <T> T executeWithLock(String lockKey, Duration waitTime, Duration leaseTime, Supplier<T> task) {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + lockKey);
        boolean acquired = false;
        try {
            acquired = lock.tryLock(waitTime.toMillis(), leaseTime.toMillis(), TimeUnit.MILLISECONDS);
            if (!acquired) {
                throw new LockAcquisitionException(lockKey);
            }
            return task.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LockAcquisitionException(lockKey, e);
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public void executeWithLock(String lockKey, Duration waitTime, Duration leaseTime, Runnable task) {
        executeWithLock(lockKey, waitTime, leaseTime, () -> {
            task.run();
            return null;
        });
    }
}
