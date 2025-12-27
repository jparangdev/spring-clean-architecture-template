package kr.co.jparangdev.domain.comment;

import java.time.LocalDateTime;

import lombok.*;

/**
 * Comment domain entity.
 */
@Getter
public class Comment {
    private final Long id;
    private String content;
    private final Long postId;
    private final Long authorId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Creates a new Comment.
     */
    public Comment(String content, Long postId, Long authorId) {
        validateContent(content);
        validatePostId(postId);
        validateAuthorId(authorId);

        this.id = null;
        this.content = content;
        this.postId = postId;
        this.authorId = authorId;
        this.createdAt = null;
        this.updatedAt = null;
    }

    /**
     * Reconstitutes an existing Comment from persisted data.
     */
    public Comment(Long id, String content, Long postId, Long authorId,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateContent(String newContent) {
        validateContent(newContent);
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canBeModifiedBy(Long userId) {
        if (userId == null) {
            return false;
        }
        return this.authorId.equals(userId);
    }

    public boolean isRecentlyCreated() {
        if (createdAt == null) {
            return false;
        }
        return createdAt.isAfter(LocalDateTime.now().minusHours(1));
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        if (content.length() > 1000) {
            throw new IllegalArgumentException("Comment content cannot exceed 1000 characters");
        }
    }

    private void validatePostId(Long postId) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }
    }

    private void validateAuthorId(Long authorId) {
        if (authorId == null) {
            throw new IllegalArgumentException("Author ID cannot be null");
        }
    }
}
