package kr.co.jparangdev.domain.model;

import java.time.LocalDateTime;

import lombok.*;

/**
 * Post 도메인 엔티티
 * 비즈니스 규칙과 불변조건을 포함합니다.
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
	 * 새 Post 생성 (ID와 타임스탬프는 영속화 시 설정됨)
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
	 * 기존 Post 재구성 (영속화된 데이터로부터)
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

	/**
	 * Post 내용 업데이트
	 * 비즈니스 규칙: 제목과 내용은 비어있을 수 없음
	 */
	public void updateContent(String newTitle, String newContent) {
		validateTitle(newTitle);
		validateContent(newContent);

		this.title = newTitle;
		this.content = newContent;
		this.updatedAt = LocalDateTime.now();
	}

	/**
	 * 특정 사용자가 이 Post를 수정할 수 있는지 확인
	 */
	public boolean canBeEditedBy(Long userId) {
		if (userId == null) {
			return false;
		}
		return this.authorId.equals(userId);
	}

	/**
	 * Post가 최근에 생성되었는지 확인 (24시간 이내)
	 */
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
