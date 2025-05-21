# 👕 무신사 과제
- 본 프로젝트는 무신사 과제 요구사항에 따라 특정 카테고리 또는 브랜드의 상품 정보를 조회하는 API를 구현한 것입니다.

## 주요 기능 및 구현 범위
본 프로젝트는 다음의 주요 기능들을 구현했습니다.

- 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
- 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을
  조회하는 API
- 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
- 브랜드 및 상품을 추가 / 업데이트 / 삭제하는 API

## 구현 조건 해석 및 가정
- 과제 요구사항에 따라, "문제에는 답이 정해져 있지 않다"는 점을 인지하고, 제시된 기능 명세를 바탕으로 합리적인 가정을 통해 기능을 구현했습니다.
- 세부적인 결정 사항이나 가정은 코드 내 주석에 명시되어 있습니다.

## 프로젝트 환경 및 사용 기술
- 언어: Java 21
- 프레임워크: Spring Boot 3.4.3
- 빌드 도구: Gradle
- 데이터베이스: H2 (In-memory, 로컬 실행용)
- 데이터베이스 마이그레이션: Flyway
- API 문서화: SpringDoc OpenAPI (Swagger UI)
- 라이브러리: Lombok, Spring Data JPA 등

## 프로젝트 구조
~~~
src
└── main
    ├── java
    │   └── com.musinsa.test  
    │       ├── config        // 애플리케이션의 설정 관련 클래스
    │       ├── controller    // HTTP 요청을 받아 처리하고 응답을 반환하는 컨트롤러 클래스
    │       ├── domain        // 비즈니스 로직의 핵심이 되는 도메인 모델
    │       ├── dto           // 데이터 전송 객체
    │       ├── exception     // 사용자 정의 예외 클래스
    │       ├── repository    // 데이터베이스 접근 로직을 담당하는 리포지토리 인터페이스
    │       ├── service       // 비즈니스 로직을 구현하는 서비스 클래스
    │       ├── util          // 공통적으로 사용되는 유틸리티 클래스
    │       └── MainApplication
    └── resources
        ├── db.migration      // Flyway 마이그레이션 스크립트
        ├── static.css        // CSS, JavaScript, 이미지 등 정적 리소스 파일
        ├── templates         // 서버 사이드 템플릿 파일 (Thymeleaf)
        └── application.properties 
~~~
___

## 설계 고려사항
### 아키텍처
- 계층형 아키텍처: 신속한 구현과 명확한 책임 분리를 위해 전통적인 Controller - Service - Repository 구조를 채택했습니다.
- 도메인 모델: 본 과제의 규모와 요구사항을 고려하여, Item이라는 단일 핵심 도메인으로 모델링하여 구현의 단순성에 초점을 맞추었습니다.

### API 응답 데이터 정렬
- API 응답 시 데이터의 정렬 순서에 대한 명시적인 요구사항이 없어, 현재는 데이터베이스 조회 결과의 자연스러운 순서 또는 주요 식별자(예: 카테고리명) 기준의 기본적인 오름차순 정렬을 따르고 있습니다.
- 기능의 정확한 동작에 중점을 두었으며, 필요시 특정 정렬 기준을 추가하는 것은 필요합니다.

### Controller 네이밍
- 과제 요구사항의 각 구현 항목을 쉽게 식별할 수 있도록, 컨트롤러는 Implement1Controller와 같이 직관적인 명칭으로 작성했습니다. 이는 과제 평가의 편의성을 위한 것입니다.

### Unit Test 를 나중에 구현한 이유
- 본 과제에서는 기능 구현 및 제출 기한을 우선적으로 고려하여, 핵심 기능 구현 완료 후 단위 테스트(Unit Test) 및 통합 테스트(Integration Test)를 작성했습니다.
- 실제 개발 프로세스에서는 테스트 주도 개발(TDD) 원칙에 따라, 기능 코드 작성 전에 테스트 케이스를 먼저 설계하고 작성하는 것이 바람직하며, 이를 통해 코드의 안정성과 설계 개선을 도모할 수 있습니다.

## 빌드, 테스트, 실행 방법
### 사전 준비
- Java 21 JDK (또는 호환 가능한 JDK)
- Gradle (프로젝트 내 Gradle Wrapper를 사용하므로 별도 설치는 필수는 아님)

### 빌드
- 프로젝트 루트 디렉토리에서 다음 명령어를 실행합니다:
~~~
bash
./gradlew clean build
(Windows의 경우: gradlew.bat clean build)
~~~

### 테스트
- 빌드 과정에 테스트가 포함되어 실행됩니다. 개별적으로 테스트만 실행하려면 다음 명령어를 사용합니다:
~~~
bash
./gradlew test
(Windows의 경우: gradlew.bat test)
~~~

### 실행
- 애플리케이션을 실행하는 방법은 다음과 같습니다:
- 빌드된 JAR 파일 실행:
~~~
bash
java -jar build/libs/musinsa-1.0-SNAPSHOT.jar
~~~
- IDE에서 실행: IntelliJ IDEA, Eclipse 등의 IDE에서 MainApplication.java 파일을 직접 실행합니다.
- 애플리케이션 실행 후, 기본적으로 H2 데이터베이스는 메모리 모드로 실행되며, Flyway에 의해 src/main/resources/db/migration 경로의 SQL 스크립트가 실행되어 초기 데이터가 구성됩니다.

___

## Frontend 페이지 구현
- 애플리케이션 실행 후 http://localhost:8080 으로 접속하면 웹 화면에서 각 기능을 확인 또는 테스트할 수 있습니다.
  - 웹 화면은 관리자 페이지를 만든다고 가정하고 심플하게 구현했습니다.
  - 각 페이지의 url 과제의 요구사항에 따라 /implement1 형태로 네이밍 했습니다.
- Swagger UI를 통한 테스트를 원하시면 http://localhost:8080/swagger-ui.html 를 이용하시면 됩니다.

### 페이지 설명
- `/implement1` - 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 페이지
- `/implement2` - 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 페이지
- `/implement3` - 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 페이지
- `/implement4` - 브랜드 및 상품을 관리(조회, 추가, 업데이트, 삭제)하는 페이지
- `/swagger-ui/index.html` - API 문서를 확인하고 테스트할 수 있는 Swagger UI 페이지
- `/h2-console/` - H2 데이터베이스 관리 콘솔 페이지 (개발 환경에서만 사용)
___

## 테스트 케이스 구현
- 단위 테스트(Unit Test)와 통합 테스트(Integration Test)를 통해 기본적인 요구사항에 대한 다양한 시나리오를 검증하며, 합리적인 가정을 바탕으로 구현된 기능이 코드의 신뢰성을 보장합니다.

## 단위 테스트 (Unit Test)
### Repository 테스트
- **ItemRepositoryTest**: 데이터 접근 계층 테스트

### Service 테스트
- **LowestPriceServiceTest**: 카테고리별 최저가 상품 조회 서비스 테스트
- **BrandPriceServiceTest**: 브랜드별 최저가 상품 조회 서비스 테스트
- **LowestHighestServiceTest**: 카테고리별 최저/최고가 상품 조회 서비스 테스트
- **ItemServiceTest**: 상품 CRUD 서비스 테스트

### 통합 테스트 (Integration Test)
- **Implement1IntegrationTest**: 카테고리별 최저가 상품 조회 API 통합 테스트
- **Implement2IntegrationTest**: 단일 브랜드 최저가 조회 API 통합 테스트
- **Implement3IntegrationTest**: 카테고리별 최저/최고가 조회 API 통합 테스트
- **Implement4IntegrationTest**: 상품 CRUD API 통합 테스트
