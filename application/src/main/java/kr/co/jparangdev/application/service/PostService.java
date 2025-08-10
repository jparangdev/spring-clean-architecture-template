package kr.co.jparangdev.application.service;

import kr.co.jparangdev.application.command.CreatePostCommand;
import kr.co.jparangdev.application.command.UpdatePostCommand;
import kr.co.jparangdev.application.dto.PostDto;
import kr.co.jparangdev.application.repository.PostRepository;
import kr.co.jparangdev.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements PostUseCase {

    private final PostRepository postRepository;

    public PostDto createPost(CreatePostCommand command) {
        Post post = new Post(null, command.getTitle(), command.getContent(), command.getAuthorId(), null, null);
        Post savedPost = postRepository.save(post);
        return toDto(savedPost);
    }

    public PostDto updatePost(UpdatePostCommand command) {
        Post post = postRepository.findById(command.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setTitle(command.getTitle());
        post.setContent(command.getContent());
        Post updatedPost = postRepository.save(post);
        return toDto(updatedPost);
    }

    public PostDto getPostById(Long id) {
        return postRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    private PostDto toDto(Post post) {
        return PostDto.from(post);
    }
}
