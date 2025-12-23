# 웹 모듈 (Web)

## 개요

웹 모듈은 표현/전달 레이어입니다. HTTP API(REST)를 노출하고 비즈니스 처리는 애플리케이션 레이어에 위임합니다. 비즈니스 규칙은 포함하지 말고, 요청 처리·검증·매핑·응답 구성에 집중하세요.

## 목적

- HTTP 요청을 처리하고 애플리케이션 유스케이스로 라우팅
- 입력값(문법적) 검증 및 의미 있는 오류 응답 제공
- 전송 계층 DTO와 애플리케이션/도메인 모델 간 매핑
- 예외 처리, 직렬화, 보안 연계 등 웹 횡단 관심사 적용

## 포함되는 것

- REST 컨트롤러(Spring @RestController)
- 웹 레이어 전용 요청/응답 DTO
- 예외 처리기(@ControllerAdvice)
- 웹 전용 설정(Jackson 모듈, CORS 등)
- 입력 검증 애노테이션 및 검증 그룹

## 포함되지 않는 것

- 비즈니스 규칙 및 도메인 불변조건
- 영속성 관심사(리포지토리, JPA 엔티티)
- 유스케이스 호출 이상의 애플리케이션 오케스트레이션 로직
- 외부 시스템 클라이언트 구현

## 의존성

- 애플리케이션 모듈에 의존(유스케이스 인터페이스/서비스)
- DAL이나 Domain에 직접 의존 금지(반드시 Application을 경유)

## 컨트롤러 가이드라인

- 컨트롤러는 얇게 유지:
  - 요청 DTO 수신 및 입력값 검증
  - 애플리케이션 서비스/유스케이스 호출
  - 응답 DTO로 매핑하고 적절한 HTTP 상태코드 설정
- 도메인 객체를 직접 노출하지 말고 DTO 사용
- 엔드포인트는 리소스 중심으로 일관되게(RESTful): 명사형, 복수형, 표준 HTTP 메서드

## DTO 가이드라인

- 형태가 다르면 요청/응답 DTO를 분리
- 가능하면 불변으로 구성; 생성자/빌더 사용
- Bean Validation(@NotNull, @Size 등)으로 검증

## 오류 처리

- 전역 예외 처리기(@ControllerAdvice)로 예외를 HTTP 응답에 매핑
- 에러 페이로드 표준화(code, message, details)
- 애플리케이션/도메인 예외를 적절한 상태코드로 매핑(400, 404, 409, 422, 500)

## 매핑

- 웹 경계에서 매핑 수행(DTO <-> 애플리케이션 모델)
- 매핑 로직 단순화; 필요 시 작은 매퍼 도입

## 보안

- 인증/인가를 웹 경계에서 적용
- 보안 판단을 컨트롤러 내부에 두지 말고 Spring Security 설정/메서드 애노테이션으로 위임

## 테스트 전략

- @WebMvcTest로 컨트롤러 슬라이스 테스트(애플리케이션 서비스는 mock)
- 요청/응답 계약 및 오류 처리용 통합 테스트
- 포맷이 중요하면 DTO JSON 직렬화 테스트

## 패키지 구조(예시)

- kr.co.jparangdev.web.controller
- kr.co.jparangdev.web.dto
- kr.co.jparangdev.web.exception
- kr.co.jparangdev.web.config
- kr.co.jparangdev.web.mapper

## 안티 패턴

- 컨트롤러에 비즈니스 로직 배치
- 도메인 엔티티를 클라이언트에 직접 반환
- 애플리케이션 레이어를 우회하여 웹에서 리포지토리 호출

## 예시

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
