# Spring Clean Architecture Template

이 프로젝트는 Spring Boot를 기반으로 클린 아키텍처를 적용한 템플릿입니다. 각 모듈은 특정 역할을 담당하며, 의존성 규칙을 통해 계층 간의 분리를 명확히 합니다.

## 모듈 구성

프로젝트는 다음과 같은 주요 모듈로 구성됩니다.

### `domain` 모듈

*   **역할:** 비즈니스 규칙과 엔티티를 정의하는 핵심 모듈입니다.
*   **특징:**
    *   가장 안쪽 계층에 해당하며, 어떤 외부 프레임워크나 기술에도 의존하지 않습니다.
    *   순수한 비즈니스 로직과 도메인 모델(엔티티, 값 객체 등)을 포함합니다.
    *   다른 모듈이 `domain` 모듈에 의존할 수는 있지만, `domain` 모듈은 다른 어떤 모듈에도 의존하지 않습니다.

### `application` 모듈

*   **역할:** 유스케이스(Use Case)를 정의하고, 도메인 계층의 비즈니스 로직을 조율하는 모듈입니다.
*   **특징:**
    *   `domain` 모듈에 의존합니다.
    *   애플리케이션의 특정 기능을 수행하기 위한 서비스 인터페이스와 구현체를 포함합니다.
    *   입력(Input)과 출력(Output) 포트(인터페이스)를 정의하여 외부 계층과의 상호작용을 추상화합니다.

### `dal` (Data Access Layer) 모듈

*   **역할:** 데이터 영속성(Persistence)을 담당하는 모듈입니다.
*   **특징:**
    *   `domain` 모듈과 `application` 모듈에 의존합니다.
    *   데이터베이스와의 상호작용을 위한 리포지토리 구현체와 데이터 매퍼(Mapper)를 포함합니다.
    *   `application` 모듈에서 정의한 리포지토리 인터페이스를 구현합니다.
    *   Spring Data JPA와 같은 영속성 프레임워크에 의존합니다.

### `web` 모듈

*   **역할:** 사용자 인터페이스(UI) 또는 외부 시스템과의 상호작용을 담당하는 모듈입니다.
*   **특징:**
    *   `application` 모듈에 의존합니다.
    *   REST 컨트롤러, DTO(Data Transfer Object), 뷰 템플릿 등을 포함합니다.
    *   HTTP 요청을 처리하고, `application` 모듈의 유스케이스를 호출하여 응답을 생성합니다.
    *   Spring Web과 같은 웹 프레임워크에 의존합니다.

### `bootapp` 모듈

*   **역할:** 애플리케이션을 실행하고 모든 모듈을 통합하는 최상위 모듈입니다.
*   **특징:**
    *   `application`, `dal`, `web` 모듈에 의존합니다.
    *   Spring Boot 애플리케이션의 메인 클래스를 포함하며, 의존성 주입(DI) 및 컴포넌트 스캔 설정을 담당합니다.
    *   애플리케이션의 시작점이며, 각 계층의 구현체들을 연결하는 역할을 합니다.

## 의존성 규칙

클린 아키텍처의 핵심 원칙에 따라, 의존성은 항상 안쪽으로 향해야 합니다. 즉, 외부 계층은 내부 계층에 의존할 수 있지만, 내부 계층은 외부 계층에 의존할 수 없습니다.

*   `bootapp` -> `web`, `dal`, `application`
*   `web` -> `application`
*   `dal` -> `application`, `domain`
*   `application` -> `domain`
*   `domain` -> (어떤 모듈에도 의존하지 않음)

이러한 구조를 통해 각 계층의 독립성을 보장하고, 비즈니스 로직이 외부 기술 변화에 영향을 받지 않도록 합니다.

---

## 모듈별 가이드라인
각 모듈의 구현 원칙과 세부 규칙은 아래 가이드라인 문서를 참고하세요. (EN/KR 제공)

- Domain
  - EN: [domain/Guideline.md](domain/Guideline.md)
  - KR: [domain/GuideLine_KR.md](domain/GuideLine_KR.md)
- Application
  - EN: [application/Guideline.md](application/Guideline.md)
  - KR: [application/GuideLine_KR.md](application/GuideLine_KR.md)
- DAL (Data Access Layer)
  - EN: [dal/Guideline.md](dal/Guideline.md)
  - KR: [dal/GuideLine_KR.md](dal/GuideLine_KR.md)
- BootApp
  - EN: [bootapp/Guideline.md](bootapp/Guideline.md)
  - KR: [bootapp/GuideLine_KR.md](bootapp/GuideLine_KR.md)
- Web
  - EN: [web/Guideline.md](web/Guideline.md)
  - KR: [web/GuideLine_KR.md](web/GuideLine_KR.md)

추가적으로, 루트의 도움말 문서도 참고할 수 있습니다: [HELP.md](HELP.md)

---

## EN/KR 언어 토글 데모 (단일 문서에서 병기)
아래는 Markdown 내 HTML `<details>`를 이용해 한 문서에서 한국어/영어를 접고 펼치는 방식으로 함께 제공하는 간단한 예시입니다. GitHub에서 안전하게 동작합니다.

<details>
  <summary><strong>한국어 보기</strong></summary>

  <p><strong>의존성 규칙</strong>: 의존성은 항상 안쪽 계층을 향해야 합니다. 외부 계층은 내부 계층에 의존할 수 있으나, 내부 계층은 외부 계층에 의존하지 않습니다.</p>
</details>

<details>
  <summary><strong>View in English</strong></summary>

  <p><strong>Dependency Rules</strong>: Dependencies must point inwards. Outer layers may depend on inner layers, but inner layers must not depend on outer layers.</p>
</details>

Tip: If you want a true tab-like UI, you can embed minimal HTML/CSS in Markdown, but some platforms (e.g., GitHub) sanitize custom CSS. The `<details>` approach is the most widely compatible.
