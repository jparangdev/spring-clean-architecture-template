# BootApp Module (Composition Root)

## Overview

The BootApp module is the composition root of the application. It wires the whole system together and starts the Spring Boot application. It should contain minimal code focused on application bootstrap, component scanning, profile and configuration management, and operational concerns (actuator, logging). It must not contain domain or application business logic.

## Purpose

- Application bootstrap: start Spring Boot and load the application context
- Composition root: configure component scanning and module wiring
- Externalized configuration: load properties via profiles (application.properties/yaml)
- Operational settings: logging, actuator exposure, metrics endpoints
- Minimal glue code that belongs to the root application class only

## What Belongs Here

- Main class annotated with `@SpringBootApplication` (e.g., SpringCleanArchitectureApplication)
- Component scanning setup to include project packages
- Spring Boot configuration files in `bootapp/src/main/resources`
- Profile selection and environment configuration
- Very limited auto-configuration toggles if needed

## What Does NOT Belong Here

- Controllers, filters, or presentation logic (belongs to Web module)
- Use case orchestration or business services (belongs to Application)
- Persistence/JPA entities and repository implementations (belongs to DAL)
- Domain models and rules (belongs to Domain)

## Dependency Rules

- Allowed
  - Depend on inner modules to start the app (Application, Web, DAL, Domain) via Gradle settings
  - Spring Boot starter dependencies (actuator, logging)
- Forbidden
  - Implementing business logic here
  - Adding tight coupling to infrastructure that breaks Clean Architecture boundaries

## Design Guidelines

- Keep this module as thin as possible; a single main class is ideal
- Use `@ComponentScan(basePackages = "kr.co.jparangdev")` (already present) to pick up beans from other modules
- Prefer configuring properties via `application.properties` and profiles rather than programmatic configuration
- Avoid placing `@Configuration` classes here unless they are truly application-wide and not specific to Web or DAL

## Configuration and Profiles

- Default configuration file: `bootapp/src/main/resources/application.properties`
- Use Spring profiles: `spring.profiles.active=local|dev|prod`
- Keep secrets out of VCS; use environment variables or externalized config for production
- Logging: configure via `logging.level.*` keys
- Actuator: expose selective endpoints for health/metrics in non-prod; secure in prod

## Testing Strategy

- Smoke tests verifying the application context loads (`@SpringBootTest` context load test)
- Health endpoint checks when actuator is enabled

## Project-specific Examples

- Main class: `kr.co.jparangdev.bootapp.SpringCleanArchitectureApplication`
- Resources: `bootapp/src/main/resources/application.properties`

## Example Structure

```
bootapp/
  src/main/java/.../SpringCleanArchitectureApplication.java
  src/main/resources/
    application.properties
```

## Best Practices

- Keep BootApp free from module-specific code; treat it purely as the entry point
- Use profiles for environment differences; avoid `if (prod)` conditionals in code
- Fail fast on misconfiguration; add meaningful startup logs
- Rely on other modules to provide beans; do not redefine them here
