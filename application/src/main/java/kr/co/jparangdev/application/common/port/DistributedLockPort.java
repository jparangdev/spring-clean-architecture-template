package kr.co.jparangdev.application.common.port;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * Distributed lock abstraction interface (Output Port).
 * Provides a technology-agnostic API for distributed locking operations.
 * Essential for coordinating access to shared resources in distributed systems.
 *
 * <p>
 * Implementations may use Redis (Redisson), Zookeeper, or any other distributed
 * lock solution.
 * </p>
 *
 * <p>
 * Usage example:
 * </p>
 * 
 * <pre>
 * String result = lockPort.executeWithLock(
 *         "order:" + orderId,
 *         Duration.ofSeconds(5), // wait up to 5 seconds to acquire
 *         Duration.ofSeconds(30), // hold lock for max 30 seconds
 *         () -> processOrder(orderId));
 * </pre>
 */
public interface DistributedLockPort {

    /**
     * Attempts to acquire a distributed lock.
     *
     * @param lockKey   unique identifier for the lock
     * @param waitTime  maximum time to wait for lock acquisition
     * @param leaseTime maximum time to hold the lock (auto-released after this
     *                  duration)
     * @return true if lock was acquired, false if timeout occurred
     */
    boolean tryLock(String lockKey, Duration waitTime, Duration leaseTime);

    /**
     * Releases a previously acquired lock.
     * Safe to call even if the lock is not held by the current thread.
     *
     * @param lockKey unique identifier for the lock
     */
    void unlock(String lockKey);

    /**
     * Executes a task while holding a distributed lock, with automatic lock
     * release.
     * This is the preferred method as it guarantees proper lock cleanup.
     *
     * @param lockKey   unique identifier for the lock
     * @param waitTime  maximum time to wait for lock acquisition
     * @param leaseTime maximum time to hold the lock
     * @param task      the task to execute while holding the lock
     * @param <T>       the return type of the task
     * @return the result of the task execution
     * @throws LockAcquisitionException if the lock cannot be acquired within
     *                                  waitTime
     */
    <T> T executeWithLock(String lockKey, Duration waitTime, Duration leaseTime, Supplier<T> task);

    /**
     * Executes a task while holding a distributed lock, with automatic lock
     * release.
     * Variant for tasks that don't return a value.
     *
     * @param lockKey   unique identifier for the lock
     * @param waitTime  maximum time to wait for lock acquisition
     * @param leaseTime maximum time to hold the lock
     * @param task      the task to execute while holding the lock
     * @throws LockAcquisitionException if the lock cannot be acquired within
     *                                  waitTime
     */
    void executeWithLock(String lockKey, Duration waitTime, Duration leaseTime, Runnable task);

    /**
     * Exception thrown when a distributed lock cannot be acquired.
     */
    class LockAcquisitionException extends RuntimeException {
        public LockAcquisitionException(String lockKey) {
            super("Failed to acquire lock: " + lockKey);
        }

        public LockAcquisitionException(String lockKey, Throwable cause) {
            super("Failed to acquire lock: " + lockKey, cause);
        }
    }
}
