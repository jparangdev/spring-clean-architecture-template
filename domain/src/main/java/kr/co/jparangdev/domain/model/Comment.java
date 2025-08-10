package kr.co.jparangdev.domain.model;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@AllArgsConstructor
public class Comment {
	private Long id;
	@Setter
	private String content;
	@Setter
	private Long postId;
	private Long authorId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
