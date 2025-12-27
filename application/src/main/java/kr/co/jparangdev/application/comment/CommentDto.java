package kr.co.jparangdev.application.comment;

import kr.co.jparangdev.domain.comment.Comment;
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
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getPostId(),
                comment.getAuthorId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt());
    }
}
