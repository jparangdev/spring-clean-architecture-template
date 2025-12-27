package kr.co.jparangdev.application.post;

import kr.co.jparangdev.application.common.exception.NotFoundException;
import kr.co.jparangdev.application.common.exception.ValidationException;
import kr.co.jparangdev.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * Post use case implementation.
 */
@Service
@RequiredArgsConstructor
public class PostService implements PostUseCase {

    private final PostRepository postRepository;
    private final TransactionTemplate transactionTemplate;
    private final TransactionTemplate readOnlyTransactionTemplate;

    @Override
    public PostDto createPost(CreatePostCommand command) {
        validateCreatePostCommand(command);

        return transactionTemplate.execute(status -> {
            try {
                Post post = new Post(
                        command.getTitle(),
                        command.getContent(),
                        command.getAuthorId());
                Post savedPost = postRepository.save(post);
                return PostDto.from(savedPost);
            } catch (IllegalArgumentException e) {
                status.setRollbackOnly();
                throw new ValidationException(e.getMessage());
            }
        });
    }

    @Override
    public PostDto updatePost(UpdatePostCommand command) {
        validateUpdatePostCommand(command);

        return transactionTemplate.execute(status -> {
            try {
                Post post = postRepository.findById(command.getId())
                        .orElseThrow(() -> new NotFoundException("Post", command.getId()));
                post.updateContent(command.getTitle(), command.getContent());
                Post updatedPost = postRepository.save(post);
                return PostDto.from(updatedPost);
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
    public PostDto getPostById(Long id) {
        if (id == null) {
            throw new ValidationException("id", "cannot be null");
        }

        return readOnlyTransactionTemplate.execute(status -> postRepository.findById(id)
                .map(PostDto::from)
                .orElseThrow(() -> new NotFoundException("Post", id)));
    }

    @Override
    public List<PostDto> getAllPosts() {
        return readOnlyTransactionTemplate.execute(status -> postRepository.findAll().stream()
                .map(PostDto::from)
                .toList());
    }

    @Override
    public void deletePost(Long id) {
        if (id == null) {
            throw new ValidationException("id", "cannot be null");
        }

        transactionTemplate.executeWithoutResult(status -> {
            if (!postRepository.findById(id).isPresent()) {
                status.setRollbackOnly();
                throw new NotFoundException("Post", id);
            }
            postRepository.deleteById(id);
        });
    }

    @Override
    public void likePost(Long id) {
        if (id == null) {
            throw new ValidationException("id", "cannot be null");
        }

        transactionTemplate.executeWithoutResult(status -> {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Post", id));
            postRepository.save(post);
        });
    }

    private void validateCreatePostCommand(CreatePostCommand command) {
        if (command == null) {
            throw new ValidationException("CreatePostCommand cannot be null");
        }
        if (command.getTitle() == null) {
            throw new ValidationException("title", "cannot be null");
        }
        if (command.getContent() == null) {
            throw new ValidationException("content", "cannot be null");
        }
        if (command.getAuthorId() == null) {
            throw new ValidationException("authorId", "cannot be null");
        }
    }

    private void validateUpdatePostCommand(UpdatePostCommand command) {
        if (command == null) {
            throw new ValidationException("UpdatePostCommand cannot be null");
        }
        if (command.getId() == null) {
            throw new ValidationException("id", "cannot be null");
        }
        if (command.getTitle() == null) {
            throw new ValidationException("title", "cannot be null");
        }
        if (command.getContent() == null) {
            throw new ValidationException("content", "cannot be null");
        }
    }
}
