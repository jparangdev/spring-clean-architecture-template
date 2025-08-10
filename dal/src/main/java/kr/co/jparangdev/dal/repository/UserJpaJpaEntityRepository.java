package kr.co.jparangdev.dal.repository;

import kr.co.jparangdev.dal.entity.UserJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaJpaEntityRepository extends BaseJpaEntityRepository<UserJpaEntity, Long> {
}
