package kr.co.jparangdev.persistence.user;

import kr.co.jparangdev.persistence.common.BaseJpaEntityRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserJpaRepository extends BaseJpaEntityRepository<UserJpaEntity, Long> {
    List<UserJpaEntity> findByLastLoginAtBeforeAndStatus(LocalDateTime dateTime, UserJpaEntity.Status status);
}
