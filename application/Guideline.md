# Application Module

## Overview

The Application module is the application layer in Clean Architecture. It expresses business use cases and orchestrates domain models while keeping domain rules protected. This layer coordinates multiple aggregates, manages transactions, and combines technical concerns (transactions, locking, retries, events) with business flows to complete a use case.

## Purpose

- Use case definition: Clearly express what the system must do
- Orchestration: Combine domain models/services to achieve the business goal
- Transaction boundary: Manage transactions at the use-case level
- Ports: Define interfaces to invert infrastructure dependencies (e.g., Repository)
- I/O models: Manage Commands/Queries/DTOs and mapping
- Protect business rules: Respect domain rules and add guards/validation around them

## What Belongs Here

- Use Case interfaces: Intent-revealing interfaces (e.g., PostUseCase)
- Application Services (use case implementations): Implement orchestration (e.g., PostService)
- Ports: Interfaces that abstract data access and external systems (e.g., PostRepository)
- Command/Query models: Explicit inputs to use cases
- DTOs: Outputs returned to outer layers
- Transaction handling: Begin/commit at the use-case boundary (@Transactional allowed)
- Cross-aggregate coordination: Flow and guard logic across multiple aggregates
- Application-level validation: Input validation and authorization checks that don’t violate domain rules

## What Does NOT Belong Here

- Pure domain rules: Core invariants of entities/value objects belong to Domain
- Infrastructure details: JPA entities, Spring Data implementations, HTTP client implementations
- Presentation logic: Controllers, request/response binding, view concerns
- Framework-heavy logic: Code tightly coupled to web or infrastructure frameworks

## Dependency Rules

- Allowed
  - Domain module: Use domain models and rules
  - Java standard library and utilities
  - Minimal Spring annotations to express the layer (@Service, @Transactional)
- Forbidden
  - DAL/Infrastructure implementation details
  - Web/Presentation layer
  - Direct dependencies on external service implementations (must go through Ports)

## Design Principles

- Define use cases as interfaces to reveal intent and ease testing
- Implement each use case in a Service; the service owns the transaction boundary
- Access domain models via Ports; implementations come from the DAL module
- Use Command/Query objects for inputs; they are stable against change
- Return DTOs; do not expose domain models directly to outer layers
- Throw meaningful exceptions (NotFound, Conflict, Validation) and let the web layer map them

## Transactions and Consistency

- One service method = one business transaction (generally)
- Separate read-only from write operations using @Transactional(readOnly = true)
- For cross-aggregate consistency, coordinate ordering/locking/events at the application layer
- Consider Saga/Outbox patterns when distributed consistency is needed

## Validation and Authorization

- Validate inputs at the Command layer (null/type/range)
- Authorization can be split: policy-based authorization in Domain, technical access checks in Application
- Protect domain rules: change state via domain methods; avoid direct field mutation

## Mapping Strategy

- Command/Query -> Domain: Create/load domain entities then invoke domain methods
- Domain -> DTO: Use dedicated factories or static helpers (e.g., Dto.from(domain))
- Minimize duplication; consider a mapper class if mappings get complex

## Testing Strategy

- Unit/Slice tests per use case: mock Ports to verify orchestration
- Include rollback and cross-aggregate scenarios
- Contract tests to keep Port–Adapter agreements stable

## Project-specific Examples

This project follows the structure below:
- UseCase interfaces: PostUseCase, UserUseCase, CommentUseCase
- Service implementations: PostService, UserService, CommentService (@Service)
- Port interfaces: PostRepository, UserRepository, CommentRepository
- I/O models: Create*Command, Update*Command, *Dto

## Example

```java
// Use Case interface: expresses intent
public interface PostUseCase {
    PostDto createPost(CreatePostCommand command);
    PostDto updatePost(UpdatePostCommand command);
    PostDto getPostById(Long id);
    List<PostDto> getAllPosts();
    void deletePost(Long id);
}

// Application Service: use-case implementation and transaction boundary
@Service
@RequiredArgsConstructor
@Transactional
public class PostService implements PostUseCase {
    private final PostRepository postRepository; // Port

    @Override
    public PostDto createPost(CreatePostCommand command) {
        if (command.getTitle() == null || command.getTitle().isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        Post post = new Post(null, command.getTitle(), command.getContent(), command.getAuthorId(), null, null);
        Post saved = postRepository.save(post);
        return PostDto.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostById(Long id) {
        return postRepository.findById(id)
            .map(PostDto::from)
            .orElseThrow(() -> new RuntimeException("Post not found"));
    }
}
```

## Best Practices

- Method names should express the use case: createPost, publishPost, addCommentToPost
- Keep services thin: domain rules in Domain; orchestration and transactions in Application
- Ports first: when a new external dependency is needed, define a Port and implement it in DAL
- Consider domain events and orchestrate follow-up actions in Application when needed
- Think idempotency for retried or duplicate requests
