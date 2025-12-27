# Spring Clean Architecture 상세 가이드

이 문서는 Spring Clean Architecture Template의 설계 철학과 각 레이어별 원칙을 설명합니다.

---

## 목차

1. [아키텍처 개요](#아키텍처-개요)
2. [Domain 레이어](#domain-레이어)
3. [Application 레이어](#application-레이어)
4. [Infrastructure 레이어](#infrastructure-레이어)
5. [Presentation 레이어](#presentation-레이어)
6. [Boot 레이어](#boot-레이어)
7. [의존성 규칙](#의존성-규칙)

---

## 아키텍처 개요

### 핵심 철학

클린 아키텍처의 핵심은 **관심사의 분리**와 **의존성 규칙**입니다:

- **안쪽으로 향하는 의존성**: 외부 레이어는 내부 레이어에 의존하지만, 그 반대는 절대 허용되지 않습니다
- **비즈니스 로직의 독립성**: 도메인과 애플리케이션 레이어는 프레임워크, 데이터베이스, UI에 독립적입니다
- **교체 가능한 세부사항**: 데이터베이스, 메시징 시스템, 외부 서비스는 비즈니스 로직 변경 없이 교체할 수 있어야 합니다

### 레이어 구조

```
┌─────────────────────────────────────────────────────────────┐
│  boot/           배포 단위 (실행 가능한 애플리케이션)         │
├────────────────────────┬────────────────────────────────────┤
│  presentation/         │  infrastructure/                   │
│  입력 어댑터           │  출력 어댑터                        │
│  (HTTP, Batch, CLI)    │  (DB, Cache, Messaging, API)       │
├────────────────────────┴────────────────────────────────────┤
│  application/          도메인 컨텍스트별 유스케이스          │
│  (user, post, comment)                                      │
├─────────────────────────────────────────────────────────────┤
│  domain/               도메인 모델 & 비즈니스 규칙          │
│  (user, post, comment)                                      │
└─────────────────────────────────────────────────────────────┘
```

---

## Domain 레이어

### 역할

비즈니스의 **핵심 개념**과 **규칙**을 정의하는 가장 안쪽 레이어입니다.

### 핵심 원칙

| 원칙 | 설명 |
|------|------|
| **외부 의존성 금지** | 프레임워크, 데이터베이스 관련 코드 사용 금지 (순수 Java만) |
| **Rich Domain Model** | 비즈니스 로직을 엔티티 메서드로 캡슐화 (Anemic Model 지양) |
| **도메인 컨텍스트 구조** | 엔티티와 관련 로직을 도메인별 패키지(user, post 등)로 그룹화 |
| **불변조건 보장** | 생성자와 메서드에서 항상 유효한 상태 검증 |
| **자기 설명적 코드** | 도메인 언어(Ubiquitous Language) 사용 |

### 포함 요소

- **엔티티 (Entity)**: 고유 식별자를 가진 도메인 객체
- **값 객체 (Value Object)**: 불변이며 식별자 없이 속성으로만 동등성 판단
- **도메인 이벤트**: 도메인에서 발생한 의미 있는 사건
- **도메인 서비스**: 특정 엔티티에 속하지 않는 비즈니스 로직

### 설계 지침

> ✅ 도메인 엔티티가 자신의 상태를 변경하는 메서드를 가져야 합니다  
> ✅ 생성 시점과 변경 시점에 불변조건을 검증해야 합니다  
> ❌ Setter를 무분별하게 노출하지 마세요  
> ❌ JPA, Spring 어노테이션을 사용하지 마세요

---

## Application 레이어

### 역할

**유스케이스**를 정의하고, 도메인 레이어의 비즈니스 로직을 **조율**합니다.

### 핵심 원칙

| 원칙 | 설명 |
|------|------|
| **UseCase 정의** | 시스템이 제공하는 기능을 인터페이스로 명시 |
| **Port 정의** | 외부 시스템과의 통신을 인터페이스로 추상화 |
| **트랜잭션 관리** | 유스케이스 단위로 트랜잭션 경계 설정 |
| **입력 검증** | Command/Query 객체에서 입력값 검증 수행 |

### 포함 요소

- **UseCase 인터페이스**: 외부에서 호출 가능한 기능 정의 (Input Port)
- **UseCase 구현체 (Service)**: 유스케이스 로직 구현
- **Output Port**: 외부 시스템 호출을 위한 인터페이스 (Repository, Cache, EventPublisher 등)
- **Command/Query**: 입력 데이터를 담는 불변 객체
- **DTO**: 출력 데이터를 담는 객체

### 설계 지침

> ✅ 각 레이어 내에서 파일들은 **도메인 컨텍스트(User, Post 등)** 기준으로 그룹화합니다.  
> ✅ Application 레이어에서 인터페이스를 정의하고, Infrastructure에서 구현합니다.
> ✅ 유스케이스 단위로 트랜잭션 경계를 명확히 합니다  
> ❌ 비즈니스 로직을 Service에 구현하지 마세요 (Domain으로 위임)  
> ❌ Infrastructure 의존성을 직접 참조하지 마세요 (Port를 통해 접근)

---

## Infrastructure 레이어

### 역할

**외부 시스템**과의 통합을 담당하는 어댑터를 구현합니다. Application 레이어의 Output Port를 구현합니다.

### 모듈 구성

| 모듈 | 책임 | 구현 대상 |
|------|------|----------|
| `persistence` | 데이터 영속화 | Repository 인터페이스 |
| `transients` | 캐싱 & 분산 락 | TransientPort (Cache, Lock) |
| `messaging` | 메시지 발행/구독 | EventPublisher |
| `client` | 외부 API 호출 | ExternalServicePort |
| `storage` | 파일 저장 | StoragePort |
| `notification` | 알림 발송 | NotificationPort |

### 핵심 원칙

| 원칙 | 설명 |
|------|------|
| **Port 구현** | Application 레이어의 인터페이스를 구현 |
| **기술 캡슐화** | 특정 기술(JPA, Redis, Kafka 등)을 내부에 캡슐화 |
| **모델 변환** | Domain ↔ Infrastructure 모델 간 매핑 담당 |

### 설계 지침

> ✅ JPA Entity, Redis Key, Kafka Message 등 기술 세부사항은 이 레이어에만 존재합니다  
> ✅ Domain 모델과 Infrastructure 모델(JPA Entity 등)을 명확히 분리합니다  
> ❌ 비즈니스 로직을 Infrastructure에 구현하지 마세요  
> ❌ Infrastructure 모델을 다른 레이어에 노출하지 마세요

---

## Presentation 레이어

### 역할

**외부 요청**을 받아 Application 레이어의 유스케이스를 호출합니다. (Input Adapter)

### 모듈 구성

| 모듈 | 책임 |
|------|------|
| `api` | REST API 엔드포인트 |
| `batch` | 배치 작업 정의 |

### 핵심 원칙

| 원칙 | 설명 |
|------|------|
| **요청 변환** | HTTP Request → Command/Query 변환 |
| **응답 생성** | DTO → HTTP Response 변환 |
| **예외 처리** | 도메인 예외 → HTTP 상태 코드 매핑 |

### 설계 지침

> ✅ Controller는 얇게 유지하고, 로직은 Application 레이어에 위임합니다  
> ✅ Request/Response 객체는 Presentation 레이어에만 존재합니다  
> ❌ Controller에서 비즈니스 로직을 구현하지 마세요  
> ❌ Domain 엔티티를 직접 응답으로 반환하지 마세요

---

## Boot 레이어

### 역할

**실행 가능한 애플리케이션**을 구성하고 필요한 모듈을 조합합니다.

### 배포 단위

| 모듈 | 역할 |
|------|------|
| `api-server` | REST API 서버 |
| `batch-server` | 배치 작업 서버 |
| `worker-server` | 비동기 작업 워커 |

### 핵심 원칙

| 원칙 | 설명 |
|------|------|
| **조합만 담당** | 비즈니스 로직 없이 모듈 조합과 설정만 담당 |
| **설정 관리** | 환경별 설정 파일 관리 |
| **의존성 주입** | 모든 구현체를 연결하는 Composition Root |

---

## 의존성 규칙

### 허용된 의존성 방향

```
boot/* 
  → presentation/*, infrastructure/*

presentation/* 
  → application (UseCase 인터페이스만 사용)

infrastructure/* 
  → application (Port 인터페이스 구현)
  → domain (모델 변환 시 참조)

application 
  → domain

domain 
  → (없음 - 순수 Java만)
```

### 핵심 규칙

1. **Domain은 아무것도 의존하지 않습니다**
2. **Application은 Domain만 의존합니다**
3. **외부 레이어는 Ports를 통해 Application과 통신합니다**
4. **구체적인 구현체는 Boot 레이어에서 조립됩니다**

---

## Docker 인프라

### 제공되는 서비스

| 서비스 | 포트 | 용도 |
|--------|------|------|
| Redis | 6379 | 캐싱, 세션, 분산 락 |
| Kafka | 9092 | 메시지 브로커 |
| Kafka UI | 8080 | 메시지 관리 UI |

### 사용법

```bash
docker-compose up -d    # 시작
docker-compose down     # 종료
```

---

## 참고 자료

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
