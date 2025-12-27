package kr.co.jparangdev.presentation.api.post;

import kr.co.jparangdev.application.post.CreatePostCommand;
import kr.co.jparangdev.application.post.UpdatePostCommand;
import kr.co.jparangdev.application.post.PostDto;
import kr.co.jparangdev.application.post.PostUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostUseCase postUseCase;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostCommand command) {
        PostDto post = postUseCase.createPost(command);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody UpdatePostCommand command) {
        command.setId(id);
        PostDto post = postUseCase.updatePost(command);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        PostDto post = postUseCase.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postUseCase.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postUseCase.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
