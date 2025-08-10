package kr.co.jparangdev.domain.model;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@AllArgsConstructor
public class User {
	private Long id;
	@Setter
	private String username;
	@Setter
	private String email;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
