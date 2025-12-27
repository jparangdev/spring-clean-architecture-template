package kr.co.jparangdev.persistence.user;

import kr.co.jparangdev.application.user.UserRepository;
import kr.co.jparangdev.domain.user.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        UserJpaEntity userJpaEntity = toJpaEntity(user);
        UserJpaEntity savedEntity = userJpaRepository.save(userJpaEntity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public List<User> findByLastLoginAtBeforeAndStatus(LocalDateTime dateTime, User.Status status) {
        UserJpaEntity.Status jpaStatus = UserJpaEntity.Status.valueOf(status.name());
        return userJpaRepository.findByLastLoginAtBeforeAndStatus(dateTime, jpaStatus).stream()
                .map(this::toDomain)
                .toList();
    }

    private UserJpaEntity toJpaEntity(User user) {
        UserJpaEntity userJpaEntity = new UserJpaEntity();
        userJpaEntity.setId(user.getId());
        userJpaEntity.setUsername(user.getUsername());
        userJpaEntity.setEmail(user.getEmail());
        userJpaEntity.setStatus(UserJpaEntity.Status.valueOf(user.getStatus().name()));
        userJpaEntity.setLastLoginAt(user.getLastLoginAt());
        userJpaEntity.setCreatedAt(user.getCreatedAt());
        userJpaEntity.setUpdatedAt(user.getUpdatedAt());
        return userJpaEntity;
    }

    private User toDomain(UserJpaEntity userJpaEntity) {
        return new User(
                userJpaEntity.getId(),
                userJpaEntity.getUsername(),
                userJpaEntity.getEmail(),
                User.Status.valueOf(userJpaEntity.getStatus().name()),
                userJpaEntity.getLastLoginAt(),
                userJpaEntity.getCreatedAt(),
                userJpaEntity.getUpdatedAt());
    }
}
