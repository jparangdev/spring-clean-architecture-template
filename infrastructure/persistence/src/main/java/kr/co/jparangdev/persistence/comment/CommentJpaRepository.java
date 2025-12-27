package kr.co.jparangdev.persistence.comment;

import kr.co.jparangdev.persistence.common.BaseJpaEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentJpaRepository extends BaseJpaEntityRepository<CommentJpaEntity, Long> {
    List<CommentJpaEntity> findByPostId(Long postId);
}
