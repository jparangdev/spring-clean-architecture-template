package kr.co.jparangdev.application.service;

import kr.co.jparangdev.application.command.CreateCommentCommand;
import kr.co.jparangdev.application.command.UpdateCommentCommand;
import kr.co.jparangdev.application.dto.CommentDto;
import kr.co.jparangdev.application.repository.CommentRepository;
import kr.co.jparangdev.domain.model.Comment;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentUseCase {

	private final CommentRepository commentRepository;

	public CommentDto createComment(CreateCommentCommand command) {
		Comment comment = new Comment(null, command.getContent(), command.getPostId(), command.getAuthorId(), null, null);
		Comment savedComment = commentRepository.save(comment);
		return toDto(savedComment);
	}

	public CommentDto updateComment(UpdateCommentCommand command) {
		Comment comment = commentRepository.findById(command.getId())
			.orElseThrow(() -> new RuntimeException("Comment not found"));
		comment.setContent(command.getContent());
		Comment updatedComment = commentRepository.save(comment);
		return toDto(updatedComment);
	}

	public CommentDto getCommentById(Long id) {
		return commentRepository.findById(id)
			.map(this::toDto)
			.orElseThrow(() -> new RuntimeException("Comment not found"));
	}

	public List<CommentDto> getCommentsByPostId(Long postId) {
		return commentRepository.findByPostId(postId).stream()
			.map(this::toDto)
			.toList();
	}

	public void deleteComment(Long id) {
		commentRepository.deleteById(id);
	}

	private CommentDto toDto(Comment comment) {
		return CommentDto.from(comment);
	}
}
