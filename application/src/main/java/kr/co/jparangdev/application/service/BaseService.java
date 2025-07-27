package kr.co.jparangdev.application.service;

/**
 * Base service interface for application services
 * @param <T> The domain entity type
 * @param <ID> The domain entity ID type
 */
public interface BaseService<T, ID> {

    /**
     * Find entity by ID
     * @param id The entity ID
     * @return The entity
     */
    T findById(ID id);

    /**
     * Save entity
     * @param entity The entity to save
     * @return The saved entity
     */
    T save(T entity);

    /**
     * Delete entity by ID
     * @param id The entity ID
     */
    void deleteById(ID id);
}
