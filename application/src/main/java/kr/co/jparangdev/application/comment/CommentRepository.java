package kr.co.jparangdev.application.comment;

import kr.co.jparangdev.domain.comment.Comment;

import java.util.List;
import java.util.Optional;

/**
 * Comment repository port interface.
 */
public interface CommentRepository {
    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    List<Comment> findByPostId(Long postId);

    void deleteById(Long id);
}
