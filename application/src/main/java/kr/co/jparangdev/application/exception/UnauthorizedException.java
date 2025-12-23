package kr.co.jparangdev.application.exception;

/**
 * 권한이 없는 작업을 시도할 때 발생하는 예외
 */
public class UnauthorizedException extends RuntimeException {
	public UnauthorizedException(String message) {
		super(message);
	}

	public UnauthorizedException(String action, Long userId) {
		super(String.format("User %d is not authorized to perform: %s", userId, action));
	}
}
