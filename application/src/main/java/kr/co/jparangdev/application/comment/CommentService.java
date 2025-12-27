package kr.co.jparangdev.application.comment;

import kr.co.jparangdev.application.common.exception.NotFoundException;
import kr.co.jparangdev.application.common.exception.ValidationException;
import kr.co.jparangdev.domain.comment.Comment;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * Comment use case implementation.
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
                Comment comment = new Comment(
                        command.getContent(),
                        command.getPostId(),
                        command.getAuthorId());
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
                Comment comment = commentRepository.findById(command.getId())
                        .orElseThrow(() -> new NotFoundException("Comment", command.getId()));
                comment.updateContent(command.getContent());
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

        return readOnlyTransactionTemplate.execute(status -> commentRepository.findById(id)
                .map(CommentDto::from)
                .orElseThrow(() -> new NotFoundException("Comment", id)));
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        if (postId == null) {
            throw new ValidationException("postId", "cannot be null");
        }

        return readOnlyTransactionTemplate.execute(status -> commentRepository.findByPostId(postId).stream()
                .map(CommentDto::from)
                .toList());
    }

    @Override
    public void deleteComment(Long id) {
        if (id == null) {
            throw new ValidationException("id", "cannot be null");
        }

        transactionTemplate.executeWithoutResult(status -> {
            if (!commentRepository.findById(id).isPresent()) {
                status.setRollbackOnly();
                throw new NotFoundException("Comment", id);
            }
            commentRepository.deleteById(id);
        });
    }

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
