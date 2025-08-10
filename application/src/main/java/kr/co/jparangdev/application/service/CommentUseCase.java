package kr.co.jparangdev.application.service;

import kr.co.jparangdev.application.command.CreateCommentCommand;
import kr.co.jparangdev.application.command.UpdateCommentCommand;
import kr.co.jparangdev.application.dto.CommentDto;

import java.util.List;

public interface CommentUseCase {
    CommentDto createComment(CreateCommentCommand command);
    CommentDto updateComment(UpdateCommentCommand command);
    CommentDto getCommentById(Long id);
    List<CommentDto> getCommentsByPostId(Long postId);
    void deleteComment(Long id);
}
