package kr.co.jparangdev.application.comment;

import java.util.List;

/**
 * Comment use case interface (input port).
 */
public interface CommentUseCase {
    CommentDto createComment(CreateCommentCommand command);

    CommentDto updateComment(UpdateCommentCommand command);

    CommentDto getCommentById(Long id);

    List<CommentDto> getCommentsByPostId(Long postId);

    void deleteComment(Long id);
}
