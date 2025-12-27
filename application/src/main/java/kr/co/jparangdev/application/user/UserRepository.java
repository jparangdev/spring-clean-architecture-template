package kr.co.jparangdev.application.user;

import kr.co.jparangdev.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * User repository port interface.
 */
public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

    List<User> findByLastLoginAtBeforeAndStatus(LocalDateTime dateTime, User.Status status);
}
