package kr.co.jparangdev.application.dto;

import kr.co.jparangdev.domain.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

	public static PostDto from(Post post) {
		PostDto dto = new PostDto();
		dto.setTitle(post.getTitle());
		dto.setContent(post.getContent());
		dto.setAuthorId(post.getAuthorId());
		return dto;
	}
}
