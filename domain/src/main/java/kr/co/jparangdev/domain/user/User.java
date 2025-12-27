package kr.co.jparangdev.domain.user;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import lombok.*;

/**
 * User domain entity.
 */
@Getter
public class User {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public enum Status {
        ACTIVE, DORMANT, DELETED
    }

    private final Long id;
    private String username;
    private String email;
    private Status status;
    private LocalDateTime lastLoginAt;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Creates a new User.
     */
    public User(String username, String email) {
        validateUsername(username);
        validateEmail(email);

        this.id = null;
        this.username = username;
        this.email = email;
        this.status = Status.ACTIVE;
        this.lastLoginAt = LocalDateTime.now();
        this.createdAt = null;
        this.updatedAt = null;
    }

    /**
     * Reconstitutes an existing User from persisted data.
     */
    public User(Long id, String username, String email, Status status, LocalDateTime lastLoginAt,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void changeUsername(String newUsername) {
        validateUsername(newUsername);
        this.username = newUsername;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeEmail(String newEmail) {
        validateEmail(newEmail);
        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProfile(String newUsername, String newEmail) {
        validateUsername(newUsername);
        validateEmail(newEmail);

        this.username = newUsername;
        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
    }

    public void login() {
        this.lastLoginAt = LocalDateTime.now();
        if (this.status == Status.DORMANT) {
            this.status = Status.ACTIVE;
        }
    }

    public void switchToDormant() {
        if (this.status == Status.ACTIVE) {
            this.status = Status.DORMANT;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void withdraw() {
        if (this.status != Status.DELETED) {
            this.status = Status.DELETED;
            this.updatedAt = LocalDateTime.now();
        }
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
