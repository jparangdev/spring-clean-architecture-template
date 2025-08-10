package kr.co.jparangdev.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserCommand {
    private Long id;
    private String username;
    private String email;
}
