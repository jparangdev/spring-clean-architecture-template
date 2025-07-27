package kr.co.jparangdev.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository interface for data access
 * @param <T> The entity type
 * @param <ID> The entity ID type
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    // Add custom repository methods here
}
