package kr.co.jparangdev.application.dto;

import kr.co.jparangdev.domain.model.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
	@Setter private String username;
    @Setter private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

	public static UserDto from(User user) {
		UserDto dto = new UserDto();
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		return dto;
	}
}
