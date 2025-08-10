package kr.co.jparangdev.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostCommand {
    private String title;
    private String content;
    private Long authorId;
}
