package kr.co.jparangdev.application.service;

import kr.co.jparangdev.application.command.CreateUserCommand;
import kr.co.jparangdev.application.command.UpdateUserCommand;
import kr.co.jparangdev.application.dto.UserDto;

import java.util.List;

public interface UserUseCase {
    UserDto createUser(CreateUserCommand command);
    UserDto updateUser(UpdateUserCommand command);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    void deleteUser(Long id);
}
