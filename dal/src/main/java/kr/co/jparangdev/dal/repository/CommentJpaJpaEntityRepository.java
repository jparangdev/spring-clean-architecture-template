package kr.co.jparangdev.dal.repository;

import kr.co.jparangdev.dal.entity.CommentJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentJpaJpaEntityRepository extends BaseJpaEntityRepository<CommentJpaEntity, Long> {
    List<CommentJpaEntity> findByPostId(Long postId);
}
