package kr.co.jparangdev.persistence.post;

import kr.co.jparangdev.persistence.common.BaseJpaEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostJpaRepository extends BaseJpaEntityRepository<PostJpaEntity, Long> {
}
