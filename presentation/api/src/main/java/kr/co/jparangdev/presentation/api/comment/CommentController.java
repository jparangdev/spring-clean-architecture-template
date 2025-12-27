package kr.co.jparangdev.presentation.api.comment;

import kr.co.jparangdev.application.comment.CreateCommentCommand;
import kr.co.jparangdev.application.comment.UpdateCommentCommand;
import kr.co.jparangdev.application.comment.CommentDto;
import kr.co.jparangdev.application.comment.CommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentUseCase commentService;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CreateCommentCommand command) {
        CommentDto comment = commentService.createComment(command);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody UpdateCommentCommand command) {
        command.setId(id);
        CommentDto comment = commentService.updateComment(command);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        CommentDto comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
