package kr.co.jparangdev.application.service;

import kr.co.jparangdev.application.command.CreateUserCommand;
import kr.co.jparangdev.application.command.UpdateUserCommand;
import kr.co.jparangdev.application.dto.UserDto;
import kr.co.jparangdev.application.exception.NotFoundException;
import kr.co.jparangdev.application.exception.ValidationException;
import kr.co.jparangdev.application.repository.UserRepository;
import kr.co.jparangdev.domain.model.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * User 유스케이스 구현
 * TransactionTemplate을 사용하여 명시적으로 트랜잭션을 관리합니다.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

	private final UserRepository userRepository;
	private final TransactionTemplate transactionTemplate;
	private final TransactionTemplate readOnlyTransactionTemplate;

	@Override
	public UserDto createUser(CreateUserCommand command) {
		validateCreateUserCommand(command);

		return transactionTemplate.execute(status -> {
			try {
				// 도메인 객체 생성 (생성자에서 검증 수행)
				User user = new User(command.getUsername(), command.getEmail());

				// 영속화
				User savedUser = userRepository.save(user);
				return UserDto.from(savedUser);

			} catch (IllegalArgumentException e) {
				status.setRollbackOnly();
				throw new ValidationException(e.getMessage());
			}
		});
	}

	@Override
	public UserDto updateUser(UpdateUserCommand command) {
		validateUpdateUserCommand(command);

		return transactionTemplate.execute(status -> {
			try {
				// 도메인 객체 로드
				User user = userRepository.findById(command.getId())
					.orElseThrow(() -> new NotFoundException("User", command.getId()));

				// 도메인 메서드 호출 (비즈니스 규칙은 도메인에서)
				user.updateProfile(command.getUsername(), command.getEmail());

				// 영속화
				User updatedUser = userRepository.save(user);
				return UserDto.from(updatedUser);

			} catch (IllegalArgumentException e) {
				status.setRollbackOnly();
				throw new ValidationException(e.getMessage());
			} catch (NotFoundException e) {
				status.setRollbackOnly();
				throw e;
			}
		});
	}

	@Override
	public UserDto getUserById(Long id) {
		if (id == null) {
			throw new ValidationException("id", "cannot be null");
		}

		return readOnlyTransactionTemplate.execute(status ->
			userRepository.findById(id)
				.map(UserDto::from)
				.orElseThrow(() -> new NotFoundException("User", id))
		);
	}

	@Override
	public List<UserDto> getAllUsers() {
		return readOnlyTransactionTemplate.execute(status ->
			userRepository.findAll().stream()
				.map(UserDto::from)
				.toList()
		);
	}

	@Override
	public void deleteUser(Long id) {
		if (id == null) {
			throw new ValidationException("id", "cannot be null");
		}

		transactionTemplate.executeWithoutResult(status -> {
			// 존재 여부 확인
			if (!userRepository.findById(id).isPresent()) {
				status.setRollbackOnly();
				throw new NotFoundException("User", id);
			}

			userRepository.deleteById(id);
		});
	}

	/**
	 * Command 레벨 입력 검증
	 */
	private void validateCreateUserCommand(CreateUserCommand command) {
		if (command == null) {
			throw new ValidationException("CreateUserCommand cannot be null");
		}
		if (command.getUsername() == null) {
			throw new ValidationException("username", "cannot be null");
		}
		if (command.getEmail() == null) {
			throw new ValidationException("email", "cannot be null");
		}
	}

	private void validateUpdateUserCommand(UpdateUserCommand command) {
		if (command == null) {
			throw new ValidationException("UpdateUserCommand cannot be null");
		}
		if (command.getId() == null) {
			throw new ValidationException("id", "cannot be null");
		}
		if (command.getUsername() == null) {
			throw new ValidationException("username", "cannot be null");
		}
		if (command.getEmail() == null) {
			throw new ValidationException("email", "cannot be null");
		}
	}
}
