# Spring Clean Architecture Template

이 프로젝트는 Spring Boot를 기반으로 클린 아키텍처를 적용한 템플릿입니다. 각 모듈은 특정 역할을 담당하며, 의존성 규칙을 통해 계층 간의 분리를 명확히 합니다.

## 주요 개선 사항

- ✅ **Rich Domain Model**: 도메인 엔티티에 비즈니스 로직과 검증 규칙 포함
- ✅ **TransactionTemplate 사용**: self-invocation 문제 방지 및 명시적 트랜잭션 관리
- ✅ **명확한 예외 처리**: NotFoundException, ValidationException 등 커스텀 예외 사용
- ✅ **다층 검증**: Command 레벨 입력 검증 + 도메인 레벨 비즈니스 규칙 검증
- ✅ **Port-Adapter 패턴**: Application 레이어에 인터페이스 정의, Infrastructure에서 구현

## 모듈 구성

프로젝트는 다음과 같은 주요 모듈로 구성됩니다.

### `domain` 모듈

*   **역할:** 비즈니스 규칙과 엔티티를 정의하는 핵심 모듈입니다.
*   **특징:**
    *   가장 안쪽 계층에 해당하며, 어떤 외부 프레임워크나 기술에도 의존하지 않습니다.
    *   순수한 비즈니스 로직과 도메인 모델(엔티티, 값 객체 등)을 포함합니다.
    *   **Rich Domain Model**: 비즈니스 규칙을 도메인 메서드로 캡슐화 (예: `post.updateContent()`)
    *   생성자에서 불변조건을 검증하여 항상 유효한 상태 보장
    *   다른 모듈이 `domain` 모듈에 의존할 수는 있지만, `domain` 모듈은 다른 어떤 모듈에도 의존하지 않습니다.

### `application` 모듈

*   **역할:** 유스케이스(Use Case)를 정의하고, 도메인 계층의 비즈니스 로직을 조율하는 모듈입니다.
*   **특징:**
    *   `domain` 모듈에 의존합니다.
    *   애플리케이션의 특정 기능을 수행하기 위한 서비스 인터페이스와 구현체를 포함합니다.
    *   입력(Input)과 출력(Output) 포트(인터페이스)를 정의하여 외부 계층과의 상호작용을 추상화합니다.
    *   **TransactionTemplate**: `@Transactional` 대신 TransactionTemplate을 사용하여 self-invocation 문제 방지
    *   **읽기/쓰기 분리**: `transactionTemplate`(쓰기), `readOnlyTransactionTemplate`(읽기)
    *   Command/Query 레벨의 입력 검증 수행
    *   커스텀 예외(NotFoundException, ValidationException)를 통한 명확한 에러 처리

### `infrastructure` 계층

Infrastructure 계층은 외부 시스템과의 통합을 담당하는 여러 하위 모듈로 구성됩니다:

#### `infrastructure/persistence` 모듈

*   **역할:** 데이터 영속성(Persistence)을 담당하는 모듈입니다.
*   **특징:**
    *   `domain` 모듈과 `application` 모듈에 의존합니다.
    *   데이터베이스와의 상호작용을 위한 리포지토리 구현체와 JPA 엔티티를 포함합니다.
    *   `application` 모듈에서 정의한 리포지토리 인터페이스를 구현합니다.
    *   도메인 모델 ↔ JPA 엔티티 간 매핑 담당
    *   Spring Data JPA와 같은 영속성 프레임워크에 의존합니다.

#### 기타 infrastructure 모듈 (확장 가능)

*   `infrastructure/client`: 외부 API 클라이언트
*   `infrastructure/messaging`: 메시징 시스템 (Kafka, RabbitMQ 등)
*   `infrastructure/storage`: 파일 스토리지 (S3, 로컬 파일 시스템 등)
*   `infrastructure/notification`: 알림 서비스 (이메일, SMS 등)

### `presentation` 계층

Presentation 계층은 다양한 인터페이스를 제공하는 여러 하위 모듈로 구성됩니다:

#### `presentation/api` 모듈

*   **역할:** REST API를 통한 외부 시스템과의 상호작용을 담당하는 모듈입니다.
*   **특징:**
    *   `application` 모듈에 의존합니다.
    *   REST 컨트롤러를 포함하며, HTTP 요청을 Command/Query로 변환합니다.
    *   `application` 모듈의 유스케이스를 호출하여 응답을 생성합니다.
    *   Spring Web과 같은 웹 프레임워크에 의존합니다.

#### `presentation/batch` 모듈 (확장 가능)

*   **역할:** 배치 작업을 위한 프레젠테이션 레이어입니다.
*   **특징:**
    *   Spring Batch 기반의 배치 작업 정의를 포함합니다.

### `boot` 계층 (배포 단위)

Boot 계층은 실행 가능한 애플리케이션들을 포함합니다:

#### `boot/api-server` 모듈

*   **역할:** API 서버를 실행하고 모든 모듈을 통합하는 최상위 모듈입니다.
*   **특징:**
    *   필요한 infrastructure 및 presentation 모듈에 의존합니다.
    *   Spring Boot 애플리케이션의 메인 클래스를 포함합니다.
    *   의존성 주입(DI) 및 컴포넌트 스캔 설정을 담당합니다.
    *   애플리케이션의 시작점이며, 각 계층의 구현체들을 연결하는 역할을 합니다.

#### 기타 boot 모듈 (확장 가능)

*   `boot/batch-server`: 배치 작업 실행 서버
*   `boot/worker-server`: 비동기 작업 처리 워커

## 의존성 규칙

클린 아키텍처의 핵심 원칙에 따라, 의존성은 항상 안쪽으로 향해야 합니다. 즉, 외부 계층은 내부 계층에 의존할 수 있지만, 내부 계층은 외부 계층에 의존할 수 없습니다.

```
boot/api-server
    ↓
presentation/api, infrastructure/persistence
    ↓
application
    ↓
domain (의존성 없음)
```

**상세 의존성:**
*   `boot/*` → `presentation/*`, `infrastructure/*`
*   `presentation/api` → `application`
*   `infrastructure/persistence` → `application`, `domain`
*   `application` → `domain`
*   `domain` → (어떤 모듈에도 의존하지 않음)

이러한 구조를 통해 각 계층의 독립성을 보장하고, 비즈니스 로직이 외부 기술 변화에 영향을 받지 않도록 합니다.

---

## 모듈별 가이드라인
각 모듈의 구현 원칙과 세부 규칙은 아래 가이드라인 문서를 참고하세요. (EN/KR 제공)

- **Domain**
  - EN: [domain/Guideline.md](domain/Guideline.md)
  - KR: [domain/GuideLine_KR.md](domain/GuideLine_KR.md)
- **Application**
  - EN: [application/Guideline.md](application/Guideline.md)
  - KR: [application/GuideLine_KR.md](application/GuideLine_KR.md)
- **Infrastructure/Persistence**
  - EN: [infrastructure/persistence/Guideline.md](infrastructure/persistence/Guideline.md)
  - KR: [infrastructure/persistence/GuideLine_KR.md](infrastructure/persistence/GuideLine_KR.md)
- **Presentation/API**
  - EN: [presentation/api/Guideline.md](presentation/api/Guideline.md)
  - KR: [presentation/api/GuideLine_KR.md](presentation/api/GuideLine_KR.md)
- **Boot/API Server**
  - EN: [boot/api-server/Guideline.md](boot/api-server/Guideline.md)
  - KR: [boot/api-server/GuideLine_KR.md](boot/api-server/GuideLine_KR.md)

추가적으로, 루트의 도움말 문서도 참고할 수 있습니다: [HELP.md](HELP.md)

## 주요 개선 사례

### 1. Rich Domain Model 예시

**Before (Anemic Domain Model):**
```java
@Getter @Setter
public class Post {
    private Long id;
    private String title;
    private String content;
    // ...
}

// 비즈니스 로직이 Service에 존재
public void updatePost(Post post, String title, String content) {
    post.setTitle(title);  // 검증 없음
    post.setContent(content);
}
```

**After (Rich Domain Model):**
```java
@Getter
public class Post {
    private String title;
    private String content;

    public void updateContent(String newTitle, String newContent) {
        validateTitle(newTitle);
        validateContent(newContent);
        this.title = newTitle;
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Post title cannot be empty");
        }
    }
}
```

### 2. TransactionTemplate 사용 예시

**Before (@Transactional의 self-invocation 문제):**
```java
@Service
public class PostService {
    @Transactional
    public void updatePost(Long id) {
        Post post = findById(id);  // self-invocation
        post.update();
    }

    @Transactional(readOnly = true)  // ⚠️ 작동하지 않음!
    public Post findById(Long id) {
        return repository.findById(id);
    }
}
```

**After (TransactionTemplate 사용):**
```java
@Service
public class PostService {
    private final TransactionTemplate transactionTemplate;
    private final TransactionTemplate readOnlyTransactionTemplate;

    public PostDto updatePost(UpdatePostCommand command) {
        return transactionTemplate.execute(status -> {
            Post post = repository.findById(command.getId())
                .orElseThrow(() -> new NotFoundException("Post", id));
            post.updateContent(command.getTitle(), command.getContent());
            return PostDto.from(repository.save(post));
        });
    }

    public PostDto getPostById(Long id) {
        return readOnlyTransactionTemplate.execute(status ->
            repository.findById(id)
                .map(PostDto::from)
                .orElseThrow(() -> new NotFoundException("Post", id))
        );
    }
}
```

### 3. 계층별 책임 분리

| 계층 | 책임 | 예시 |
|------|------|------|
| **Domain** | 비즈니스 규칙, 불변조건 | `post.updateContent()`, `user.changeEmail()` |
| **Application** | 유스케이스 조율, 트랜잭션, 입력 검증 | Command 검증, 트랜잭션 관리, DTO 변환 |
| **Infrastructure** | 기술 상세 구현 | JPA 엔티티, 리포지토리 구현, DB 접근 |
| **Presentation** | 요청/응답 변환, HTTP 처리 | REST 컨트롤러, Request/Response 매핑 |