package kr.co.jparangdev.dal.repository.impl;

import kr.co.jparangdev.application.repository.UserRepository;
import kr.co.jparangdev.dal.entity.UserJpaEntity;
import kr.co.jparangdev.dal.repository.UserJpaJpaEntityRepository;
import kr.co.jparangdev.domain.model.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaJpaEntityRepository userJpaRepository;

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

	private UserJpaEntity toJpaEntity(User user) {
		UserJpaEntity userJpaEntity = new UserJpaEntity();
		userJpaEntity.setId(user.getId());
		userJpaEntity.setUsername(user.getUsername());
		userJpaEntity.setEmail(user.getEmail());
		userJpaEntity.setCreatedAt(user.getCreatedAt());
		userJpaEntity.setUpdatedAt(user.getUpdatedAt());
		return userJpaEntity;
	}

	private User toDomain(UserJpaEntity userJpaEntity) {
		return new User(
			userJpaEntity.getId(),
			userJpaEntity.getUsername(),
			userJpaEntity.getEmail(),
			userJpaEntity.getCreatedAt(),
			userJpaEntity.getUpdatedAt()
		);
	}
}
