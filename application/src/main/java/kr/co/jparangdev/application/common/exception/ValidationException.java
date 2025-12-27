package kr.co.jparangdev.application.common.exception;

/**
 * Exception thrown when input validation fails.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String field, String reason) {
        super(String.format("Validation failed for field '%s': %s", field, reason));
    }
}
