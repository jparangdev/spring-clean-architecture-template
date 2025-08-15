# Domain Module

## Overview

The Domain module is the innermost layer of Clean Architecture. It holds the core business model and rules and must remain independent from frameworks, databases, and UI. It represents the heart of the business and is stable against technical change.

## Purpose

- Pure business logic: define what the system is and can do without technical concerns
- Framework independence: use only the Java standard library and small utility libs
- Aggregate root management: model aggregates and invariants (DDD principles)
- Business rule enforcement: keep critical rules inside entities and value objects

## What Belongs Here

- Domain Entities: core business objects (aggregate roots and entities)
- Value Objects: immutable types that describe characteristics and concepts
- Domain Services: domain logic that does not fit a single entity
- Business Rules and Invariants
- Domain Events: business-significant occurrences

## What Does NOT Belong Here

- Infrastructure dependencies: database access, HTTP clients, external service calls
- Framework annotations/dependencies (Spring, JPA, Jakarta EE, etc.)
- Presentation/UI logic
- Application orchestration (use cases, transactions)

## Dependency Rules

### Allowed Dependencies
- Utility libraries only (e.g., Apache Commons, Guava) when they don’t leak infrastructure
- Java Standard Library
- Immutable data libraries

### Forbidden Dependencies
- Spring Framework or any DI/web annotations
- JPA/Hibernate or persistence concerns
- Web frameworks (REST, MVC, etc.)
- External service clients

## Design Guidelines

- Entity design
  - Keep entities focused on business behavior (avoid anemic models)
  - Favor immutability; expose intent-revealing methods for state changes
  - Enforce invariants in constructors and methods
- Communication across aggregates
  - Prefer domain events to coordinate between aggregates
  - Keep aggregates small, cohesive, and consistent
- Business rule implementation
  - Implement rules as methods on entities/value objects
  - Use domain services when logic spans multiple entities/aggregates

## Testing Strategy

- Unit-test entities, value objects, and domain services with no frameworks
- Property-based tests for critical invariants and value objects (optional)
- Test domain events emission and resulting state changes

## Project-specific Examples

- Domain models in this project: Post, User, Comment
- Entities are plain Java objects using Lombok for boilerplate (no framework annotations)
- Business methods (e.g., Post.updateContent) encapsulate rules

## Example

```java
// Good: Business logic within domain entity
public class Post {
    // ... fields
    
    public void updateContent(String newContent) {
        if (newContent == null || newContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Post content cannot be empty");
        }
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean canBeEditedBy(Long userId) {
        return this.authorId.equals(userId);
    }
}
```

## Best Practices

- Keep behavior with data; avoid setter-driven state changes
- Isolate domain from technical concerns; no framework imports here
- Model ubiquitous language explicitly with entities/value objects
- Use small, intention-revealing methods to enforce rules
- Prefer domain events for cross-aggregate communication
