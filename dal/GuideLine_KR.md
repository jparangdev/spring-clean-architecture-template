# DAL (Data Access Layer) 모듈

## 개요

DAL 모듈은 데이터 영속성과 캐싱을 위한 구체 어댑터를 제공합니다. 애플리케이션 레이어의 포트(예: 리포지토리)를 구현하며, JPA 엔티티, Spring Data 리포지토리, MySQL 연결, Redis 통합 등 기술 세부사항을 포함합니다. 이 모듈은 클린 아키텍처의 바깥쪽 레이어에 위치하며 비즈니스 규칙을 포함해서는 안 됩니다.

## 목적

- 애플리케이션 포트 구현: 애플리케이션 모듈에 정의된 리포지토리 인터페이스의 구체 구현 제공
- 데이터 영속성과 캐싱: 관계형 DB(예: MySQL), 캐시(예: Redis) 및 기타 데이터 소스와 상호작용
- 매핑: 영속성 모델(JPA 엔티티, 캐시 표현)과 도메인 모델 간 변환
- 설정: 데이터 레이어 설정(JPA, 트랜잭션 매니저, Redis 템플릿, 커넥션 풀)

## 포함되는 것

- 영속성 모델과 산출물
  - JPA 엔티티와 임베더블(예: *JpaEntity 클래스)
  - Spring Data 리포지토리 및 커스텀 리포지토리 구현체
  - 데이터베이스 전용 스크립트/마이그레이션(필요 시)
- 포트 구현
  - 애플리케이션 리포지토리 인터페이스를 구현하는 클래스(예: PostRepositoryImpl)
  - 도메인 모델과 JPA 엔티티/DTO 간 매퍼
- 데이터 레이어 설정
  - JPA/Hibernate 설정(예: JpaConfig)
  - DataSource, EntityManager, TransactionManager 설정
  - Redis 설정(템플릿, 시리얼라이저)
- 외부 데이터 저장소 통합
  - 드라이버와 클라이언트: MySQL, Redis, JDBC 등

## 포함되지 않는 것

- 도메인 비즈니스 규칙이나 불변조건
- 애플리케이션 오케스트레이션 로직과 유스케이스 서비스
- 웹/프레젠테이션 레이어(컨트롤러, 뷰 모델, 요청/응답 바인딩)
- 데이터 접근을 넘어서는 교차 서비스 비즈니스 워크플로우

## 의존성 규칙

- 허용
  - 도메인 모듈(도메인 모델과 매핑 대상)
  - 애플리케이션 모듈(포트 인터페이스 구현을 위해)
  - Spring Data JPA, Hibernate, JDBC, DB 드라이버(예: MySQL), Redis 클라이언트 라이브러리
  - 매핑 유틸리티(예: MapStruct)와 Lombok
- 금지
  - 웹/프레젠테이션 의존성
  - Web 모듈 또는 컨트롤러에 대한 직접 의존
  - Domain이나 Application이 다시 DAL에 의존하게 만드는 어떤 의존도

참고: 바깥 레이어(DAL)는 안쪽 레이어(Domain, Application)에 의존할 수 있지만, 그 반대는 불가능합니다.

## 설계 가이드라인

- 포트–어댑터 패턴
  - 인터페이스(Port)는 애플리케이션 모듈에 둠
  - 이곳에서 해당 인터페이스를 충족하는 어댑터를 구현
- 매핑
  - 영속성 엔티티와 도메인 모델은 분리 유지
  - Domain <-> 영속성 간 변환을 명시적으로 제공(수동 또는 매퍼 활용)
- 트랜잭션
  - 트랜잭션 경계는 일반적으로 애플리케이션 서비스가 소유
  - 기술적 필요 시 리포지토리 메서드에서 제한적으로 @Transactional 사용 가능하나, 비즈니스 트랜잭션 오케스트레이션은 지양
- 성능과 정확성
  - 조회 작업에는 읽기 전용 쿼리를 선호
  - 적절한 인덱싱과 페이징 사용
  - N+1 문제는 가져오기 전략으로 대응

## 테스트 전략

- 실제 또는 임베디드 DB로 리포지토리 통합 테스트
- JPA 엔티티와 도메인 모델 간 매핑 정확성 테스트
- 애플리케이션 포트 계약을 만족하는지 검증하는 계약 테스트
- 선택적으로 Testcontainers로 MySQL/Redis 사용

## 프로젝트 예시

본 프로젝트는 다음을 포함합니다:
- JPA 엔티티: PostJpaEntity, UserJpaEntity, CommentJpaEntity
- Spring Data 리포지토리: BaseJpaEntityRepository, *JpaEntityRepository
- 포트 구현: PostRepositoryImpl, UserRepositoryImpl, CommentRepositoryImpl (애플리케이션 리포지토리 구현체)
- 설정: JpaConfig

## 예시 구조

```
dal/
  src/main/java/..../entity/
    PostJpaEntity.java
  src/main/java/..../repository/
    PostJpaEntityRepository.java  // Spring Data
  src/main/java/..../repository/impl/
    PostRepositoryImpl.java       // Application의 PostRepository 구현
  src/main/java/..../config/
    JpaConfig.java
```

## 모범 사례

- 리포지토리 구현에서는 비즈니스 로직을 배제하고, 영속성 관심사에 집중
- 애플리케이션 포트만 의존하도록 하여 DAL을 교체 가능하게 유지
- 영속성 제약 위반 시 의미 있는 예외로 빠르게 실패
- 기술 세부사항(JPA 애노테이션, Redis 템플릿)은 이 모듈로 격리
- 중요한 쿼리와 인덱스를 문서화
