package kr.co.jparangdev.application.service;

import kr.co.jparangdev.application.command.CreateCommentCommand;
import kr.co.jparangdev.application.command.UpdateCommentCommand;
import kr.co.jparangdev.application.dto.CommentDto;
import kr.co.jparangdev.application.exception.NotFoundException;
import kr.co.jparangdev.application.exception.ValidationException;
import kr.co.jparangdev.application.repository.CommentRepository;
import kr.co.jparangdev.domain.model.Comment;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * Comment 유스케이스 구현
 * TransactionTemplate을 사용하여 명시적으로 트랜잭션을 관리합니다.
 */
@Service
@RequiredArgsConstructor
public class CommentService implements CommentUseCase {

	private final CommentRepository commentRepository;
	private final TransactionTemplate transactionTemplate;
	private final TransactionTemplate readOnlyTransactionTemplate;

	@Override
	public CommentDto createComment(CreateCommentCommand command) {
		validateCreateCommentCommand(command);

		return transactionTemplate.execute(status -> {
			try {
				// 도메인 객체 생성 (생성자에서 검증 수행)
				Comment comment = new Comment(
					command.getContent(),
					command.getPostId(),
					command.getAuthorId()
				);

				// 영속화
				Comment savedComment = commentRepository.save(comment);
				return CommentDto.from(savedComment);

			} catch (IllegalArgumentException e) {
				status.setRollbackOnly();
				throw new ValidationException(e.getMessage());
			}
		});
	}

	@Override
	public CommentDto updateComment(UpdateCommentCommand command) {
		validateUpdateCommentCommand(command);

		return transactionTemplate.execute(status -> {
			try {
				// 도메인 객체 로드
				Comment comment = commentRepository.findById(command.getId())
					.orElseThrow(() -> new NotFoundException("Comment", command.getId()));

				// 도메인 메서드 호출 (비즈니스 규칙은 도메인에서)
				comment.updateContent(command.getContent());

				// 영속화
				Comment updatedComment = commentRepository.save(comment);
				return CommentDto.from(updatedComment);

			} catch (IllegalArgumentException e) {
				status.setRollbackOnly();
				throw new ValidationException(e.getMessage());
			} catch (NotFoundException e) {
				status.setRollbackOnly();
				throw e;
			}
		});
	}

	@Override
	public CommentDto getCommentById(Long id) {
		if (id == null) {
			throw new ValidationException("id", "cannot be null");
		}

		return readOnlyTransactionTemplate.execute(status ->
			commentRepository.findById(id)
				.map(CommentDto::from)
				.orElseThrow(() -> new NotFoundException("Comment", id))
		);
	}

	@Override
	public List<CommentDto> getCommentsByPostId(Long postId) {
		if (postId == null) {
			throw new ValidationException("postId", "cannot be null");
		}

		return readOnlyTransactionTemplate.execute(status ->
			commentRepository.findByPostId(postId).stream()
				.map(CommentDto::from)
				.toList()
		);
	}

	@Override
	public void deleteComment(Long id) {
		if (id == null) {
			throw new ValidationException("id", "cannot be null");
		}

		transactionTemplate.executeWithoutResult(status -> {
			// 존재 여부 확인
			if (!commentRepository.findById(id).isPresent()) {
				status.setRollbackOnly();
				throw new NotFoundException("Comment", id);
			}

			commentRepository.deleteById(id);
		});
	}

	/**
	 * Command 레벨 입력 검증
	 */
	private void validateCreateCommentCommand(CreateCommentCommand command) {
		if (command == null) {
			throw new ValidationException("CreateCommentCommand cannot be null");
		}
		if (command.getContent() == null) {
			throw new ValidationException("content", "cannot be null");
		}
		if (command.getPostId() == null) {
			throw new ValidationException("postId", "cannot be null");
		}
		if (command.getAuthorId() == null) {
			throw new ValidationException("authorId", "cannot be null");
		}
	}

	private void validateUpdateCommentCommand(UpdateCommentCommand command) {
		if (command == null) {
			throw new ValidationException("UpdateCommentCommand cannot be null");
		}
		if (command.getId() == null) {
			throw new ValidationException("id", "cannot be null");
		}
		if (command.getContent() == null) {
			throw new ValidationException("content", "cannot be null");
		}
	}
}
