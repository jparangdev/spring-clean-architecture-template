package kr.co.jparangdev.domain.model;

import java.time.LocalDateTime;

import lombok.*;

/**
 * Comment 도메인 엔티티
 * 비즈니스 규칙과 불변조건을 포함합니다.
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
	 * 새 Comment 생성
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
	 * 기존 Comment 재구성 (영속화된 데이터로부터)
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

	/**
	 * Comment 내용 수정
	 */
	public void updateContent(String newContent) {
		validateContent(newContent);
		this.content = newContent;
		this.updatedAt = LocalDateTime.now();
	}

	/**
	 * 특정 사용자가 이 Comment를 수정/삭제할 수 있는지 확인
	 */
	public boolean canBeModifiedBy(Long userId) {
		if (userId == null) {
			return false;
		}
		return this.authorId.equals(userId);
	}

	/**
	 * Comment가 최근에 작성되었는지 확인 (1시간 이내)
	 */
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
