package kr.co.jparangdev.application.user;

import java.util.List;

/**
 * User use case interface (input port).
 */
public interface UserUseCase {
    UserDto createUser(CreateUserCommand command);

    UserDto updateUser(UpdateUserCommand command);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    void deleteUser(Long id);

    void changePassword(Long userId, String newPassword);
}
