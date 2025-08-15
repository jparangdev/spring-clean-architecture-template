# 애플리케이션 모듈 (Application)

## 개요

애플리케이션 모듈은 클린 아키텍처의 애플리케이션 레이어입니다. 비즈니스 유스케이스를 표현하고 도메인 모델을 오케스트레이션하며, 도메인의 규칙을 보호합니다. 이 레이어는 여러 애그리게이트를 조정하고 트랜잭션을 관리하며, 비즈니스 플로우를 완수하기 위해 기술적 관심사(트랜잭션, 락, 재시도, 이벤트)를 결합합니다.

## 목적

- 유스케이스 정의: 시스템이 무엇을 해야 하는지 명확히 표현
- 오케스트레이션: 도메인 모델/서비스를 결합하여 비즈니스 목표 달성
- 트랜잭션 경계: 유스케이스 레벨에서 트랜잭션 관리
- 포트(Ports): 인프라 의존성을 역전시키는 인터페이스 정의(예: Repository)
- I/O 모델: Command/Query/DTO 및 매핑 관리
- 비즈니스 규칙 보호: 도메인 규칙을 존중하고 그 주변에서 가드/검증 수행

## 포함되는 것

- 유스케이스 인터페이스: 의도를 드러내는 인터페이스(예: PostUseCase)
- 애플리케이션 서비스(유스케이스 구현): 오케스트레이션 구현(예: PostService)
- 포트: 데이터 접근 및 외부 시스템을 추상화한 인터페이스(예: PostRepository)
- 커맨드/쿼리 모델: 유스케이스의 명시적 입력
- DTO: 외부 레이어로 반환되는 출력 모델
- 트랜잭션 처리: 유스케이스 경계에서 시작/커밋(@Transactional 허용)
- 애그리게이트 간 조율: 여러 애그리게이트에 걸친 플로우/가드 로직
- 애플리케이션 수준 검증: 도메인 규칙을 침해하지 않는 입력 검증과 인가 체크

## 포함되지 않는 것

- 순수 도메인 규칙: 엔티티/값 객체의 핵심 불변조건은 Domain에 위치
- 인프라 상세: JPA 엔티티, Spring Data 구현, HTTP 클라이언트 구현
- 프레젠테이션 로직: 컨트롤러, 요청/응답 바인딩, 뷰 관련
- 프레임워크-의존적 로직: 웹이나 인프라 프레임워크에 강하게 결합된 코드

## 의존성 규칙

- 허용
  - 도메인 모듈: 도메인 모델과 규칙 사용
  - 자바 표준 라이브러리 및 유틸리티
  - 레이어 표현을 위한 최소한의 스프링 애노테이션(@Service, @Transactional)
- 금지
  - DAL/인프라 구현 상세에 대한 의존
  - 웹/프레젠테이션 레이어에 대한 의존
  - 외부 서비스 구현에 직접 의존(반드시 포트를 통해 접근)

## 설계 원칙

- 유스케이스를 의도를 드러내는 인터페이스로 정의하고, 테스트 용이성을 높임
- 각 유스케이스를 Service로 구현하고, 서비스가 트랜잭션 경계를 소유
- 도메인 모델 접근은 Port를 통해 수행하고, 구현체는 DAL 모듈에서 제공
- 입력은 Command/Query 객체 사용; 변화에 안정적인 형태 유지
- 출력은 DTO 반환; 외부 레이어에 도메인 모델을 직접 노출하지 않음
- 의미 있는 예외(예: NotFound, Conflict, Validation)를 던지고, 매핑은 웹 레이어에서 처리

## 트랜잭션과 일관성

- 일반적으로 한 서비스 메서드 = 하나의 비즈니스 트랜잭션
- 읽기 전용 작업은 @Transactional(readOnly = true)로 분리
- 애그리게이트 간 일관성은 애플리케이션 레이어에서 순서/락/이벤트를 통해 조정
- 분산 일관성이 필요하면 Saga/Outbox 패턴 고려

## 검증과 인가

- 입력 검증은 Command 레벨에서 수행(null/타입/범위 등)
- 인가는 분리 가능: 정책 기반 인가는 Domain, 기술적 접근 제어는 Application
- 도메인 규칙 보호: 상태 변경은 도메인 메서드를 통해 수행하고, 직접 필드 변경 지양

## 매핑 전략

- Command/Query -> Domain: 도메인 엔티티 생성/로딩 후 도메인 메서드 호출
- Domain -> DTO: 전용 팩토리 또는 정적 헬퍼 사용(예: Dto.from(domain))
- 중복 최소화; 매핑이 복잡해지면 전용 매퍼 클래스 고려

## 테스트 전략

- 유스케이스별 단위/슬라이스 테스트: Port를 목킹하여 오케스트레이션 검증
- 롤백과 애그리게이트 간 시나리오 포함
- Port–Adapter 계약을 안정적으로 유지하기 위한 계약 테스트

## 프로젝트 예시

본 프로젝트는 다음 구조를 따릅니다:
- 유스케이스 인터페이스: PostUseCase, UserUseCase, CommentUseCase
- 서비스 구현: PostService, UserService, CommentService (@Service)
- 포트 인터페이스: PostRepository, UserRepository, CommentRepository
- I/O 모델: Create*Command, Update*Command, *Dto

## 예시

```java
// 유스케이스 인터페이스: 의도를 표현
public interface PostUseCase {
    PostDto createPost(CreatePostCommand command);
    PostDto updatePost(UpdatePostCommand command);
    PostDto getPostById(Long id);
    List<PostDto> getAllPosts();
    void deletePost(Long id);
}

// 애플리케이션 서비스: 유스케이스 구현과 트랜잭션 경계
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

## 모범 사례

- 메서드 이름은 유스케이스를 표현해야 함: createPost, publishPost, addCommentToPost
- 서비스는 슬림하게 유지: 도메인 규칙은 Domain, 오케스트레이션과 트랜잭션은 Application
- 우선 Port: 새로운 외부 의존이 필요하면 Port를 정의하고 DAL에서 구현
- 도메인 이벤트를 고려하고, 필요한 경우 애플리케이션에서 후속 작업을 오케스트레이션
- 재시도/중복 요청에 대한 멱등성 고려
