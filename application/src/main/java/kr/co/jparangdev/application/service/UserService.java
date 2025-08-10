package kr.co.jparangdev.application.service;

import kr.co.jparangdev.application.command.CreateUserCommand;
import kr.co.jparangdev.application.command.UpdateUserCommand;
import kr.co.jparangdev.application.dto.UserDto;
import kr.co.jparangdev.application.repository.UserRepository;
import kr.co.jparangdev.domain.model.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

	private final UserRepository userRepository;

	public UserDto createUser(CreateUserCommand command) {
		User user = new User(null, command.getUsername(), command.getEmail(), null, null);
		User savedUser = userRepository.save(user);
		return toDto(savedUser);
	}

	public UserDto updateUser(UpdateUserCommand command) {
		User user = userRepository.findById(command.getId())
			.orElseThrow(() -> new RuntimeException("User not found"));
		user.setUsername(command.getUsername());
		user.setEmail(command.getEmail());
		User updatedUser = userRepository.save(user);
		return toDto(updatedUser);
	}

	public UserDto getUserById(Long id) {
		return userRepository.findById(id)
			.map(this::toDto)
			.orElseThrow(() -> new RuntimeException("User not found"));
	}

	public List<UserDto> getAllUsers() {
		return userRepository.findAll().stream()
			.map(this::toDto)
			.toList();
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	private UserDto toDto(User user) {
		return UserDto.from(user);
	}
}
