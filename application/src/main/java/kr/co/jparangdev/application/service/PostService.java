package kr.co.jparangdev.application.service;

import kr.co.jparangdev.application.command.CreatePostCommand;
import kr.co.jparangdev.application.command.UpdatePostCommand;
import kr.co.jparangdev.application.dto.PostDto;
import kr.co.jparangdev.application.exception.NotFoundException;
import kr.co.jparangdev.application.exception.ValidationException;
import kr.co.jparangdev.application.repository.PostRepository;
import kr.co.jparangdev.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * Post 유스케이스 구현
 * TransactionTemplate을 사용하여 명시적으로 트랜잭션을 관리하고 self-invocation 문제를 회피합니다.
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
                // 도메인 객체 생성 (생성자에서 검증 수행)
                Post post = new Post(
                    command.getTitle(),
                    command.getContent(),
                    command.getAuthorId()
                );

                // 영속화
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
                // 도메인 객체 로드
                Post post = postRepository.findById(command.getId())
                    .orElseThrow(() -> new NotFoundException("Post", command.getId()));

                // 도메인 메서드 호출 (비즈니스 규칙은 도메인에서)
                post.updateContent(command.getTitle(), command.getContent());

                // 영속화
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

        return readOnlyTransactionTemplate.execute(status ->
            postRepository.findById(id)
                .map(PostDto::from)
                .orElseThrow(() -> new NotFoundException("Post", id))
        );
    }

    @Override
    public List<PostDto> getAllPosts() {
        return readOnlyTransactionTemplate.execute(status ->
            postRepository.findAll().stream()
                .map(PostDto::from)
                .toList()
        );
    }

    @Override
    public void deletePost(Long id) {
        if (id == null) {
            throw new ValidationException("id", "cannot be null");
        }

        transactionTemplate.executeWithoutResult(status -> {
            // 존재 여부 확인
            if (!postRepository.findById(id).isPresent()) {
                status.setRollbackOnly();
                throw new NotFoundException("Post", id);
            }

            postRepository.deleteById(id);
        });
    }

    /**
     * Command 레벨 입력 검증
     */
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
