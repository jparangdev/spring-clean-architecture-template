package kr.co.jparangdev.application.post;

import kr.co.jparangdev.domain.post.Post;

import java.util.List;
import java.util.Optional;

/**
 * Post repository port interface.
 */
public interface PostRepository {
    Post save(Post post);

    Optional<Post> findById(Long id);

    List<Post> findAll();

    void deleteById(Long id);
}
