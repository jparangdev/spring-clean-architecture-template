package kr.co.jparangdev.domain.model;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@AllArgsConstructor
public class Post {
	private Long id;
	@Setter
	private String title;
	@Setter
	private String content;
	private Long authorId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
