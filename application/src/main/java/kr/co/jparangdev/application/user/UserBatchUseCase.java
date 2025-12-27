package kr.co.jparangdev.application.user;

/**
 * User batch operation use case interface.
 */
public interface UserBatchUseCase {
    /**
     * Processes users who haven't logged in for a long time and marks them as
     * dormant.
     *
     * @return the number of users processed
     */
    int processDormantUsers();
}
