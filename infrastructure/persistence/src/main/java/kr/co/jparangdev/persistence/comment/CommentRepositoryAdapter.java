package kr.co.jparangdev.persistence.comment;

import kr.co.jparangdev.application.comment.CommentRepository;
import kr.co.jparangdev.domain.comment.Comment;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryAdapter implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        CommentJpaEntity commentJpaEntity = toJpaEntity(comment);
        CommentJpaEntity savedEntity = commentJpaRepository.save(commentJpaEntity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return commentJpaRepository.findByPostId(postId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        commentJpaRepository.deleteById(id);
    }

    private CommentJpaEntity toJpaEntity(Comment comment) {
        CommentJpaEntity commentJpaEntity = new CommentJpaEntity();
        commentJpaEntity.setId(comment.getId());
        commentJpaEntity.setContent(comment.getContent());
        commentJpaEntity.setPostId(comment.getPostId());
        commentJpaEntity.setAuthorId(comment.getAuthorId());
        commentJpaEntity.setCreatedAt(comment.getCreatedAt());
        commentJpaEntity.setUpdatedAt(comment.getUpdatedAt());
        return commentJpaEntity;
    }

    private Comment toDomain(CommentJpaEntity commentJpaEntity) {
        return new Comment(
                commentJpaEntity.getId(),
                commentJpaEntity.getContent(),
                commentJpaEntity.getPostId(),
                commentJpaEntity.getAuthorId(),
                commentJpaEntity.getCreatedAt(),
                commentJpaEntity.getUpdatedAt());
    }
}
