package kr.co.jparangdev.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository interface for data access
 * @param <T> The entity type
 * @param <I> The entity ID type
 */
@NoRepositoryBean
public interface BaseJpaEntityRepository<T, I> extends JpaRepository<T, I> {
    // Add custom repository methods here
}
