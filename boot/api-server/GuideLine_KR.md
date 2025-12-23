# 부트앱 모듈 (BootApp, Composition Root)

## 개요

BootApp 모듈은 애플리케이션의 컴포지션 루트입니다. 전체 시스템을 조립하고 Spring Boot 애플리케이션을 시작합니다. 이 모듈은 애플리케이션 부트스트랩, 컴포넌트 스캔, 프로파일/설정 관리, 운영 관점(액추에이터, 로깅)에 집중하는 최소한의 코드만 포함해야 합니다. 도메인이나 애플리케이션 비즈니스 로직은 포함하지 않습니다.

## 목적

- 애플리케이션 부트스트랩: Spring Boot를 시작하고 애플리케이션 컨텍스트 로드
- 컴포지션 루트: 컴포넌트 스캔과 모듈 간 연결 구성
- 외부화된 설정: 프로파일을 통한 프로퍼티 로드(application.properties/yaml)
- 운영 설정: 로깅, 액추에이터 노출, 메트릭 엔드포인트
- 루트 애플리케이션 클래스에만 필요한 최소한의 글루 코드

## 포함되는 것

- `@SpringBootApplication`이 붙은 메인 클래스(예: SpringCleanArchitectureApplication)
- 프로젝트 패키지를 포함하는 컴포넌트 스캔 설정
- `bootapp/src/main/resources`의 Spring Boot 설정 파일
- 프로파일 선택 및 환경 설정
- 필요한 경우 매우 제한적인 오토 설정 토글

## 포함되지 않는 것

- 컨트롤러, 필터 등 프레젠테이션 로직(Web 모듈 소관)
- 유스케이스 오케스트레이션이나 비즈니스 서비스(Application 모듈 소관)
- 영속성/JPA 엔티티와 리포지토리 구현(DAL 모듈 소관)
- 도메인 모델과 규칙(Domain 모듈 소관)

## 의존성 규칙

- 허용
  - Gradle 설정을 통해 앱 부팅에 필요한 내부 모듈(Application, Web, DAL, Domain)에 의존
  - Spring Boot 스타터 의존성(액추에이터, 로깅)
- 금지
  - 이곳에서 비즈니스 로직을 구현
  - 클린 아키텍처 경계를 깨는 인프라에 대한 강결합 추가

## 설계 가이드라인

- 이 모듈은 가능한 얇게 유지; 단일 메인 클래스가 이상적
- 다른 모듈의 빈을 가져오기 위해 `@ComponentScan(basePackages = "kr.co.jparangdev")` 사용(이미 적용됨)
- 프로퍼티와 프로파일을 통한 설정을 선호하고, 프로그래밍 방식 설정은 최소화
- Web이나 DAL에 특화된 설정이 아니라면, `@Configuration` 클래스는 가급적 두지 않음

## 설정과 프로파일

- 기본 설정 파일: `bootapp/src/main/resources/application.properties`
- 스프링 프로파일 사용: `spring.profiles.active=local|dev|prod`
- 비밀정보는 VCS에 두지 말고, 프로덕션에선 환경변수나 외부 설정 사용
- 로깅: `logging.level.*` 키로 구성
- 액추에이터: 논프로드에서 헬스/메트릭 일부만 노출, 프로드에서는 보안 적용

## 테스트 전략

- 애플리케이션 컨텍스트 로딩을 검증하는 스모크 테스트(`@SpringBootTest`)
- 액추에이터 사용 시 헬스 엔드포인트 확인

## 프로젝트 예시

- 메인 클래스: `kr.co.jparangdev.bootapp.SpringCleanArchitectureApplication`
- 리소스: `bootapp/src/main/resources/application.properties`

## 예시 구조

```
bootapp/
  src/main/java/.../SpringCleanArchitectureApplication.java
  src/main/resources/
    application.properties
```

## 모범 사례

- BootApp에는 모듈 특화 코드를 두지 말고, 순수 진입점으로 취급
- 환경 차이는 프로파일로 처리; 코드에 `if (prod)` 같은 분기 지양
- 설정 오류 시 빠르게 실패하고 의미 있는 시작 로그 추가
- 다른 모듈이 빈을 제공하도록 하고, 이곳에서 재정의하지 않음
