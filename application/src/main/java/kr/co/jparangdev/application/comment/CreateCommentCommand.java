package kr.co.jparangdev.application.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentCommand {
    private String content;
    private Long postId;
    private Long authorId;
}
