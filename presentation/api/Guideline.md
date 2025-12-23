# Web Module

## Overview

The Web module is the presentation and delivery layer. It exposes HTTP APIs (REST) and delegates business operations to the Application layer. It must not contain business rules; instead it should focus on request handling, validation, mapping, and response shaping.

## Purpose

- Handle HTTP requests and route them to application use cases
- Validate inputs (syntactic validation) and return meaningful errors
- Map between transport-layer DTOs and application/domain models
- Apply cross-cutting web concerns (exception handling, serialization, security integration)

## What Belongs Here

- REST Controllers (Spring @RestController)
- Request/Response DTOs specific to the web layer
- Exception handlers (e.g., @ControllerAdvice)
- Web-specific configuration (e.g., Jackson modules, CORS)
- Input validation annotations and groups

## What Does NOT Belong Here

- Business rules and domain invariants
- Persistence concerns (repositories, JPA entities)
- Application orchestration logic beyond invoking a use case
- External system client implementations

## Dependencies

- Depends on the Application module (use case interfaces/services)
- Must not depend on DAL or directly on Domain (go through Application)

## Controller Guidelines

- Controllers should be thin:
  - Accept request DTOs, validate inputs
  - Call Application services/use cases
  - Map results to response DTOs and set proper HTTP status codes
- Avoid leaking domain objects; use DTOs for all public APIs
- Keep endpoints resource-oriented and consistent (RESTful): nouns, pluralization, standard HTTP methods

## DTO Guidelines

- Separate request and response DTOs when shapes differ
- Keep DTOs immutable where possible; use builders or constructors
- Validate with Bean Validation annotations (@NotNull, @Size, etc.)

## Error Handling

- Use a global exception handler (@ControllerAdvice) to map exceptions to HTTP responses
- Standardize error payloads (code, message, details)
- Map application/domain exceptions to appropriate HTTP statuses (400, 404, 409, 422, 500)

## Mapping

- Perform mapping at the web boundary (DTO <-> application models)
- Keep mapping logic simple; consider small mappers where needed

## Security

- Apply authentication/authorization at the web boundary
- Do not embed security decisions in controllers; delegate to Spring Security configuration and method-level annotations when appropriate

## Testing Strategy

- Slice tests for controllers with @WebMvcTest (mock application services)
- Integration tests for request/response contracts and error handling
- JSON serialization tests for DTOs when formats are critical

## Package Structure (Example)

- kr.co.jparangdev.web.controller
- kr.co.jparangdev.web.dto
- kr.co.jparangdev.web.exception
- kr.co.jparangdev.web.config
- kr.co.jparangdev.web.mapper

## Anti-patterns

- Putting business logic in controllers
- Returning domain entities directly to clients
- Bypassing the application layer to call repositories from web

## Example

```java
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
  private final PostUseCase postUseCase;

  @PostMapping
  public ResponseEntity<PostResponse> create(@Valid @RequestBody CreatePostRequest req) {
    var cmd = new CreatePostCommand(req.getAuthorId(), req.getTitle(), req.getContent());
    var result = postUseCase.createPost(cmd);
    var body = PostResponse.from(result);
    return ResponseEntity.status(HttpStatus.CREATED).body(body);
  }
}
```
