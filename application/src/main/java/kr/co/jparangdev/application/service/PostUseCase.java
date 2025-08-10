package kr.co.jparangdev.application.service;

import kr.co.jparangdev.application.command.CreatePostCommand;
import kr.co.jparangdev.application.command.UpdatePostCommand;
import kr.co.jparangdev.application.dto.PostDto;

import java.util.List;

public interface PostUseCase {
    PostDto createPost(CreatePostCommand command);
    PostDto updatePost(UpdatePostCommand command);
    PostDto getPostById(Long id);
    List<PostDto> getAllPosts();
    void deletePost(Long id);
}
