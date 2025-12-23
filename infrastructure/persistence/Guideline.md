# DAL (Data Access Layer) Module

## Overview

The DAL module provides concrete adapters for data persistence and caching. It implements the Application layer Ports (e.g., repositories) and contains technology-specific details such as JPA entities, Spring Data repositories, MySQL connectivity, and Redis integrations. This module sits on the outer layer in Clean Architecture and must not contain business rules.

## Purpose

- Implement Application Ports: Provide concrete implementations for repository interfaces defined in the Application module
- Data persistence and caching: Interact with relational databases (e.g., MySQL), caches (e.g., Redis), and other data sources
- Mapping: Convert between persistence models (JPA entities, cache representations) and Domain models
- Configuration: Hold data-layer configuration (JPA, transaction manager, Redis templates, connection pools)

## What Belongs Here

- Persistence models and artifacts
  - JPA entities and embeddables (e.g., *JpaEntity classes)
  - Spring Data repositories and custom repository implementations
  - Database-specific scripts/migrations (if applicable)
- Port implementations
  - Classes that implement Application repository interfaces (e.g., PostRepositoryImpl)
  - Mappers to convert between Domain models and JPA entities/DTOs
- Data-layer configuration
  - JPA/Hibernate configuration (e.g., JpaConfig)
  - DataSource, EntityManager, TransactionManager configuration
  - Redis configuration (templates, serializers)
- Integration with external data stores
  - Drivers and clients: MySQL, Redis, JDBC, etc.

## What Does NOT Belong Here

- Domain business rules or invariants
- Application orchestration logic and use-case services
- Web/Presentation layer (controllers, view models, request/response binding)
- Cross-service communication logic beyond data access (e.g., business workflows)

## Dependency Rules

- Allowed
  - Domain module (for Domain models and mapping targets)
  - Application module (to implement Port interfaces)
  - Spring Data JPA, Hibernate, JDBC, database drivers (e.g., MySQL), Redis client libraries
  - Mapping utilities (e.g., MapStruct) and Lombok
- Forbidden
  - Web/Presentation dependencies
  - Direct dependencies on the Web module or controllers
  - Any dependency that would force the Domain or Application module to depend back on DAL

Note: Outer layers (DAL) may depend on inner layers (Domain, Application), but not vice versa.

## Design Guidelines

- Port–Adapter pattern
  - Keep interfaces (Ports) in the Application module
  - Implement adapters here to fulfill those interfaces
- Mapping
  - Keep persistence entities separate from domain models
  - Provide explicit mapping (manual or via a mapper) to and from Domain
- Transactions
  - Transaction boundaries are typically owned by Application services
  - Use @Transactional in DAL sparingly (e.g., for repository methods if needed by the chosen tech), but avoid orchestrating business transactions here
- Performance and correctness
  - Prefer read-only queries for fetch operations
  - Use proper indexing and pagination on database access
  - Handle N+1 issues with fetch strategies where relevant

## Testing Strategy

- Repository integration tests with an actual or embedded database
- Tests for mapping correctness between JPA entities and Domain models
- Contract tests to ensure implementations satisfy Application Port contracts
- Optional testcontainers for MySQL/Redis

## Project-specific Examples

This project includes:
- JPA Entities: PostJpaEntity, UserJpaEntity, CommentJpaEntity
- Spring Data Repositories: BaseJpaEntityRepository, *JpaEntityRepository
- Port Implementations: PostRepositoryImpl, UserRepositoryImpl, CommentRepositoryImpl (implement Application repositories)
- Configuration: JpaConfig

## Example Structure

```
dal/
  src/main/java/..../entity/
    PostJpaEntity.java
  src/main/java/..../repository/
    PostJpaEntityRepository.java  // Spring Data
  src/main/java/..../repository/impl/
    PostRepositoryImpl.java       // Implements application PostRepository
  src/main/java/..../config/
    JpaConfig.java
```

## Best Practices

- Keep business logic out of repository implementations; focus on persistence concerns
- Keep DAL replaceable by relying solely on Application Ports
- Fail fast with meaningful exceptions when persistence constraints are violated
- Isolate technology details (JPA annotations, Redis templates) to this module only
- Document critical queries and indexes
