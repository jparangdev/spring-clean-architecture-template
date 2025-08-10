package kr.co.jparangdev.application.repository;

import kr.co.jparangdev.domain.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    List<Comment> findByPostId(Long postId);
    void deleteById(Long id);
}
