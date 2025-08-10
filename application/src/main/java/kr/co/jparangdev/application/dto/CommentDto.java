package kr.co.jparangdev.application.dto;

import kr.co.jparangdev.domain.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
	private Long id;
	private String content;
	private Long postId;
	private Long authorId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static CommentDto from(Comment comment) {
		CommentDto dto = new CommentDto();
		dto.setPostId(comment.getPostId());
		dto.setContent(comment.getContent());
		dto.setAuthorId(comment.getAuthorId());
		return dto;
	}
}
