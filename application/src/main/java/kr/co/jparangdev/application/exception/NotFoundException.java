package kr.co.jparangdev.application.exception;

/**
 * 요청한 리소스를 찾을 수 없을 때 발생하는 예외
 */
public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String resourceType, Long id) {
		super(String.format("%s with id %d not found", resourceType, id));
	}
}
