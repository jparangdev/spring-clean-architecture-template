# Spring Clean Architecture Template

A Spring Boot multi-module project template implementing **Clean Architecture** principles with clear separation of concerns and dependency rules.

## 🎯 Concept

This template demonstrates how to structure a Spring Boot application following Clean Architecture, where:
- **Dependencies point inward** - outer layers depend on inner layers, never the reverse
- **Business logic is isolated** - domain and application layers have no framework dependencies
- **Infrastructure is pluggable** - databases, messaging, and external services can be swapped without affecting business logic

## 📦 Module Structure

```
┌─────────────────────────────────────────────────────────────┐
│                        boot/                                │
│   (api-server, batch-server, worker-server)                 │
│   Deployable applications that wire everything together     │
├────────────────────────┬────────────────────────────────────┤
│   presentation/        │        infrastructure/             │
│   (api, batch)         │   (persistence, cache, messaging)  │
│   Input adapters       │   Output adapters                  │
├────────────────────────┴────────────────────────────────────┤
│                      application/                           │
│     Use cases organized by domain (user, post, etc.)        │
├─────────────────────────────────────────────────────────────┤
│                        domain/                              │
│     Entities & business rules (user, post, etc.)            │
└─────────────────────────────────────────────────────────────┘
```

### Layer Responsibilities

| Layer | Purpose | Key Principle |
|-------|---------|---------------|
| **domain** | Core business logic & entities | Zero external dependencies, organized by domain context |
| **application** | Use cases & coordination | Defines ports, manages transactions, organized by domain context |
| **infrastructure** | Technical implementations | Implements output ports (DB, cache, messaging, etc.) |
| **presentation** | Input adapters | REST APIs, Batch jobs, CLI handlers |
| **boot** | Deployment units | Composition root, configuration, and startup logic |

## 🏗️ Infrastructure Modules

| Module | Responsibility | Technologies |
|--------|----------------|--------------|
| `persistence` | Database access | Spring Data JPA |
| `transients` | Caching & distributed locks | Redis |
| `messaging` | Event publishing | Kafka |
| `client` | External API calls | RestTemplate, WebClient |
| `storage` | File storage | S3, local filesystem |
| `notification` | Notifications | Email, SMS, Push |

## 🚀 Quick Start

```bash
# Start infrastructure (Redis + Kafka)
docker-compose up -d

# Build the project
./gradlew build

# Run the API server
./gradlew :boot:api-server:bootRun
```

## 📚 Documentation

For detailed implementation guidelines in Korean:
- **[상세 아키텍처 가이드 (Korean)](docs/ARCHITECTURE_KR.md)** - 각 레이어별 구현 원칙과 예시

## 🔑 Key Design Decisions

- **Rich Domain Model** - Business logic lives in domain entities, not services
- **Port-Adapter Pattern** - Application defines interfaces, infrastructure implements
- **Multi-module Gradle** - Each layer is a separate module for clear boundaries

## 📄 License

MIT License