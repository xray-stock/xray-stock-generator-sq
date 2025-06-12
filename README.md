## 📈 xray-stock-generator-sq

### 프로젝트 개요

**xray-stock-generator-sq**는 다양한 종목의 가상 주식 시세 데이터를 실시간으로 생성하고, 이를 Redis를 통해 저장·조회하는 미들웨어 서비스입니다. 테스트, 개발, 데모 환경에서 실제 주식시장과 유사한 Ticker 데이터를 API로 간편하게 제공하는 것이 목적입니다.

---

### 시스템 구조 및 주요 기능

#### 1. 도메인 및 시뮬레이션 설계

- **종목 특성 관리**  
  각 주식 종목(삼성전자, Apple, Tesla 등)은 `StockTickerType` enum으로 통합 관리됩니다.  
  종목마다 시장(KOSPI/NASDAQ), 심볼, 소숫점 자리수, 변동폭(±N%), 최대 거래량, 초기 가격 등 시세 시뮬레이션에 필요한 모든 파라미터가 Enum에 정의되어 있습니다.

- **Tick 데이터 구조**  
  도메인 객체 `TradeTick`은 종목코드, 가격, 등락률, 거래량, 마지막 갱신시각을 포함하여, 실제 주식 거래 Tick 데이터를 모방합니다.

- **등락률(변동폭) 유연성**  
  각 종목의 시세 변동폭(±N%)은 `FluctuationPercent` Value Object로 관리되어, 종목별·상황별로 실제 시장과 유사한 변동성을 시뮬레이션할 수 있습니다.

#### 2. 시세 생성 및 저장

- **랜덤 Tick 생성기**  
  `RandomTickerV1Generator`가 각 Tick마다 종목별 정책(변동폭, 소숫점 등)에 따라 가격·등락률·거래량을 생성합니다.

- **주기적 자동 생성**  
  `TickerGenerateScheduler`가 1초마다 모든 종목별로 새로운 Tick 데이터를 만들어 Redis에 저장합니다.

- **데이터 저장/조회 계층 분리**  
  저장/조회 인터페이스(Port)와 실제 구현(Repository, Redis 연동)을 분리하여, 확장성과 테스트 용이성을 높였습니다.

- **Redis Stream 활용**  
  Redis는 Stream 구조로 사용되어, 시점별·구간별 Tick 데이터를 빠르고 효율적으로 저장·조회합니다.

#### 3. API 및 외부 노출

- **RESTful API 구조**  
  API는 `/api/v1/stocks/{symbol}/trade-ticks` 경로를 기준으로, 다양한 데이터 조회 방식을 지원합니다.
    - `/at?at=시각`: 특정 시점의 Tick 데이터
    - `/range?from=시작시각&to=종료시각`: 구간 내 Tick 데이터 리스트
    - `/latest`: 가장 최근의 Tick 데이터

- **유스케이스 기반 서비스 계층**  
  각 API는 서비스 계층(UseCase)을 통해 도메인·저장소 분리 및 비즈니스 로직 캡슐화를 달성합니다.

- **일관된 예외 처리**  
  존재하지 않는 종목(symbol) 등 주요 예외 상황은 전역 컨트롤러 어드바이스에서 일관된 JSON 에러 응답으로 처리합니다.

#### 4. 전체 패키지 구조

```
src/main/java/app/xray/stock/stock_generator/
├── domain/                   # 도메인 객체 및 Enum, Value Object 등
│   ├── TradeTick.java
│   ├── StockTickerType.java
│   └── FluctuationPercent.java
├── adapter/
│   ├── in/
│   │   ├── job/TickerGenerateScheduler.java   # 자동 Tick 생성 스케줄러
│   │   └── web/StocksController.java         # REST API 컨트롤러
│   ├── out/
│   │   ├── persistence/RedisTradeTickQueryRepository.java   # Redis 저장/조회
│   │   └── simulator/RandomTickDataGenerateQueryRepository.java # 테스트용
├── application/
│   ├── port/in/                              # 유스케이스 인터페이스
│   ├── port/out/                             # 저장/조회 인터페이스
│   └── service/                              # 서비스 구현
├── common/
│   ├── util/DataSerializer.java              # 직렬화/역직렬화
│   ├── util/RoundUtil.java                   # 소숫점 반올림 등
│   └── advice/GlobalControllerAdvice.java    # 전역 예외처리
```

---

### 동작 흐름

1. **Tick 데이터 생성**  
   `TickerGenerateScheduler`가 1초마다 종목별로 랜덤 시세 데이터를 생성하여 Redis에 저장합니다.
2. **API 데이터 제공**  
   - `/trade-ticks/latest`: 최신 Tick 조회
   - `/trade-ticks/at?at=시각`: 특정 시점 Tick 조회
   - `/trade-ticks/range?from=시작&to=끝`: 구간별 Tick 데이터 조회  
   각 API 요청은 서비스 계층의 유스케이스를 거쳐, 도메인 객체를 응답용 DTO로 변환하여 반환합니다.
3. **예외 상황**  
   존재하지 않는 종목 요청시, 글로벌 예외 처리기로 404 에러 및 상세 메시지를 반환합니다.

---

### 예시 API

```http
GET /api/v1/stocks/AAPL/trade-ticks/latest
GET /api/v1/stocks/AAPL/trade-ticks/at?at=2025-06-12T00:00:00Z
GET /api/v1/stocks/AAPL/trade-ticks/range?from=2025-06-12T00:00:00Z&to=2025-06-12T01:00:00Z
```
각각 최신, 특정 시점, 구간의 가상 시세 데이터(JSON) 반환

---

### 실행 방법

1. Java 17+ 및 Redis 환경 준비
2. `application.yml`에서 Redis 연결 정보 세팅
3. 프로젝트 빌드 및 실행
4. REST API 호출로 데이터 확인

---

### 기술 스택

- Java 17+
- Spring Boot
- Spring Data Redis
- Lombok
- Jackson

---

### 참고

- 본 프로젝트는 실제 투자 목적이 아닌, 테스트/데모/개발 환경용 가상 주식 시세 데이터 시뮬레이터입니다.
