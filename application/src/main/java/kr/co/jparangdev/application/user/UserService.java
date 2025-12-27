package kr.co.jparangdev.application.user;

import kr.co.jparangdev.application.common.exception.NotFoundException;
import kr.co.jparangdev.application.common.exception.ValidationException;
import kr.co.jparangdev.domain.user.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * User use case implementation.
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
                User user = new User(command.getUsername(), command.getEmail());
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
                User user = userRepository.findById(command.getId())
                        .orElseThrow(() -> new NotFoundException("User", command.getId()));
                user.updateProfile(command.getUsername(), command.getEmail());
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

        return readOnlyTransactionTemplate.execute(status -> userRepository.findById(id)
                .map(UserDto::from)
                .orElseThrow(() -> new NotFoundException("User", id)));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return readOnlyTransactionTemplate.execute(status -> userRepository.findAll().stream()
                .map(UserDto::from)
                .toList());
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null) {
            throw new ValidationException("id", "cannot be null");
        }

        transactionTemplate.executeWithoutResult(status -> {
            if (!userRepository.findById(id).isPresent()) {
                status.setRollbackOnly();
                throw new NotFoundException("User", id);
            }
            userRepository.deleteById(id);
        });
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        if (userId == null || newPassword == null) {
            throw new ValidationException("Invalid input for password change");
        }

        transactionTemplate.executeWithoutResult(status -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User", userId));
            userRepository.save(user);
        });
    }

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
