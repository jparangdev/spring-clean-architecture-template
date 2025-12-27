package kr.co.jparangdev.application.post;

import java.util.List;

/**
 * Post use case interface (input port).
 */
public interface PostUseCase {
    PostDto createPost(CreatePostCommand command);

    PostDto updatePost(UpdatePostCommand command);

    PostDto getPostById(Long id);

    List<PostDto> getAllPosts();

    void deletePost(Long id);

    void likePost(Long id);
}
