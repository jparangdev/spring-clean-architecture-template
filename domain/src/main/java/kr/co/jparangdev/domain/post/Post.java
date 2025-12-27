package kr.co.jparangdev.domain.post;

import java.time.LocalDateTime;

import lombok.*;

/**
 * Post domain entity.
 */
@Getter
public class Post {
    private final Long id;
    private String title;
    private String content;
    private final Long authorId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Creates a new Post.
     */
    public Post(String title, String content, Long authorId) {
        validateTitle(title);
        validateContent(content);
        validateAuthorId(authorId);

        this.id = null;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.createdAt = null;
        this.updatedAt = null;
    }

    /**
     * Reconstitutes an existing Post from persisted data.
     */
    public Post(Long id, String title, String content, Long authorId,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateContent(String newTitle, String newContent) {
        validateTitle(newTitle);
        validateContent(newContent);

        this.title = newTitle;
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canBeEditedBy(Long userId) {
        if (userId == null) {
            return false;
        }
        return this.authorId.equals(userId);
    }

    public boolean isRecentlyCreated() {
        if (createdAt == null) {
            return false;
        }
        return createdAt.isAfter(LocalDateTime.now().minusDays(1));
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Post title cannot be empty");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("Post title cannot exceed 200 characters");
        }
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Post content cannot be empty");
        }
        if (content.length() > 10000) {
            throw new IllegalArgumentException("Post content cannot exceed 10000 characters");
        }
    }

    private void validateAuthorId(Long authorId) {
        if (authorId == null) {
            throw new IllegalArgumentException("Author ID cannot be null");
        }
    }
}
