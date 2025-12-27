package kr.co.jparangdev.application.common.exception;

/**
 * Exception thrown when an unauthorized operation is attempted.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String action, Long userId) {
        super(String.format("User %d is not authorized to perform: %s", userId, action));
    }
}
