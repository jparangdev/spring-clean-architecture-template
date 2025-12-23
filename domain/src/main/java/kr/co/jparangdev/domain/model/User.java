package kr.co.jparangdev.domain.model;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import lombok.*;

/**
 * User 도메인 엔티티
 * 비즈니스 규칙과 불변조건을 포함합니다.
 */
@Getter
public class User {
	private static final Pattern EMAIL_PATTERN =
		Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

	private final Long id;
	private String username;
	private String email;
	private final LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	/**
	 * 새 User 생성
	 */
	public User(String username, String email) {
		validateUsername(username);
		validateEmail(email);

		this.id = null;
		this.username = username;
		this.email = email;
		this.createdAt = null;
		this.updatedAt = null;
	}

	/**
	 * 기존 User 재구성 (영속화된 데이터로부터)
	 */
	public User(Long id, String username, String email,
	            LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	/**
	 * 사용자명 변경
	 */
	public void changeUsername(String newUsername) {
		validateUsername(newUsername);
		this.username = newUsername;
		this.updatedAt = LocalDateTime.now();
	}

	/**
	 * 이메일 변경
	 */
	public void changeEmail(String newEmail) {
		validateEmail(newEmail);
		this.email = newEmail;
		this.updatedAt = LocalDateTime.now();
	}

	/**
	 * 프로필 업데이트 (username과 email을 함께 변경)
	 */
	public void updateProfile(String newUsername, String newEmail) {
		validateUsername(newUsername);
		validateEmail(newEmail);

		this.username = newUsername;
		this.email = newEmail;
		this.updatedAt = LocalDateTime.now();
	}

	private void validateUsername(String username) {
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username cannot be empty");
		}
		if (username.length() < 3) {
			throw new IllegalArgumentException("Username must be at least 3 characters");
		}
		if (username.length() > 50) {
			throw new IllegalArgumentException("Username cannot exceed 50 characters");
		}
	}

	private void validateEmail(String email) {
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email cannot be empty");
		}
		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new IllegalArgumentException("Invalid email format");
		}
	}
}
