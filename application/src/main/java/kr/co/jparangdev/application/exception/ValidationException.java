package kr.co.jparangdev.application.exception;

/**
 * 입력 검증이 실패했을 때 발생하는 예외
 */
public class ValidationException extends RuntimeException {
	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(String field, String reason) {
		super(String.format("Validation failed for field '%s': %s", field, reason));
	}
}
