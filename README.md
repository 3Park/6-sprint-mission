
### 프로젝트 요약
- RESTful API, WebSocket, SSE를 이용한 채팅 API 서비스
  - 회원 가입 등 회원 관리
  - JWT 토큰을 이용한 인증 / 인가
  - 채팅방 및 메시지 생성 / 관리
  - 알림 기능
  - kafka를 이용한 이벤트로깅 및 전달
  - Redis 및 Caffein 캐싱 사용
  - Actuator, Prometheus, Grafana
  - Docker 및 Github Action CI/CD
  - AWS ECS, ECR, RDS 등

### Language & Runtime

- **Java 17** (Gradle Toolchain)

### Build & Test

- **Gradle**
- **JaCoCo** (Test Coverage Report – XML / HTML)

### Framework

- **Spring Boot 3.4.0**

### Web & Communication

- **Spring Web (REST API)**
- **Spring WebSocket**
- **Spring Security Messaging**

### Security

- **Spring Security**
- **JWT (Nimbus JOSE + JWT)**

### Persistence & Data

- **Spring Data JPA**
- **PostgreSQL** (Production Database)
- **H2 Database** (Test Database)

### Messaging & Streaming

- **Apache Kafka**
    - Spring Kafka

### Cache & Performance

- **Spring Cache**
- **Redis**
- **Caffeine Cache**

### Reliability

- **Spring Retry**

### Cloud & External Services

- **AWS S3**
    - AWS SDK v2

### API Documentation

- **Springdoc OpenAPI 3**
    - Swagger UI 제공

### Validation & Monitoring

- **Spring Validation (Bean Validation)**
- **Spring Actuator**

### Code Generation & Productivity

- **Lombok**
- **MapStruct** (DTO ↔ Entity Mapping)

### Testing

- **JUnit 5 (JUnit Platform)**
- **Spring Boot Test**
