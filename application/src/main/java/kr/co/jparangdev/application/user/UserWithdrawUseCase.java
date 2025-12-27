package kr.co.jparangdev.application.user;

/**
 * User withdrawal use case interface.
 */
public interface UserWithdrawUseCase {
    /**
     * Withdraws a user from the system.
     *
     * @param userId the ID of the user to withdraw
     */
    void withdraw(Long userId);
}
