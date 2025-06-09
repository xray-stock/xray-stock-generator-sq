## 📈 xray-stock-generator-sq

### 프로젝트 개요

**xray-stock-generator-sq**는 실시간 가상 주식 시세 데이터를 자동 생성하고, Redis를 활용해 저장/조회하는 미들웨어 서비스입니다.  
테스트, 개발, 데모 환경에서 실제와 유사한 주식 Ticker 데이터를 API로 제공하는 것이 목적입니다.

---

### 개발 및 구현 내용 (2025년 6월 기준 최신)

#### 1. 가상 주식 시세(Ticker) 생성 로직
- **종목별 특성값 Enum화**  
  - `StockTickerType` enum에서 각 종목(삼성전자, Apple, Tesla 등)의 시장(KOSPI, NASDAQ), 심볼, 소숫점 자리수, 변동폭(±N%), 최대 거래량, 초기 가격 등을 통합 관리합니다.
  - 심볼 기반 조회 및 초기 Ticker 객체 생성 지원.
- **랜덤 Ticker 생성기**  
  - `RandomTickerV1Generator` 클래스를 통해 각 Tick(1초 단위 등)마다 변동폭에 맞는 랜덤 가격·등락률·거래량 생성.
  - 변동폭(`FluctuationPercent`)은 Value Object로 설계되어 종목별로 ±1%~3% 내외 등락률을 유연하게 설정 가능.

#### 2. 저장/조회 구조 및 확장성
- **Port & Repository Pattern 도입**  
  - 저장/조회 인터페이스(`SaveTickDataPort`, `LoadTickDataPort`)와 실제 구현(`RedisTickDataCommandRepository`, `RedisTickDataQueryRepository`)를 분리해 테스트 용이성과 확장성 확보.
- **Redis 기반 데이터 저장/조회**  
  - 생성된 Ticker는 Redis에 저장되며, 시점별/종목별로 신속히 조회 가능.
- **Jackson 기반 직렬화/역직렬화**  
  - `DataSerializer` 유틸을 통해 Ticker 객체를 JSON으로 변환/복원. Java Time 등 특수 타입 지원.

#### 3. API 및 외부 노출
- **RESTful API 제공**  
  - `/api/v1/stocks/{symbol}/tickers` 엔드포인트에서 실시간(또는 특정 시점) Ticker 조회.
  - 데이터 부재 시 빈 데이터 응답.
- **Spring Boot 기반 서비스**  
  - `StockGeneratorApplication`이 엔트리포인트, 스케줄러/컨트롤러/서비스 계층이 도메인-어댑터 구조로 분리.

#### 4. 주요 클래스 구조 (폴더링)
```
src/main/java/app/xray/stock/stock_generator/
├── StockGeneratorApplication.java
├── domain/
│   ├── Ticker.java                        # 시세 도메인 (심볼, 가격, 변동률, 거래량, 갱신시각)
│   ├── StockTickerType.java               # 종목별 특성값 Enum
│   ├── FluctuationPercent.java            # 변동폭 Value Object
│   └── RandomTickerV1Generator.java       # 랜덤 데이터 생성기
├── adapter/
│   ├── in/
│   │   ├── job/TickerGenerateScheduler.java   # 주기적 Ticker 생성/저장
│   │   └── web/StockQuotesController.java    # REST API
│   ├── out/
│   │   ├── persistence/
│   │   │   ├── RedisTickDataCommandRepository.java # Redis 저장
│   │   │   └── RedisTickDataQueryRepository.java   # Redis 조회
│   │   └── simulator/RandomTickDataGenerateQueryRepository.java # (테스트용) 랜덤 데이터 조회
├── common/util/
│   └── DataSerializer.java                 # JSON 직렬화/역직렬화
├── application/
│   ├── port/in/                            # 유스케이스 인터페이스
│   ├── port/out/                           # 저장/조회 인터페이스
│   └── service/
│       └── TickDataQueryService.java       # 시세 데이터 조회 서비스
```

#### 5. 동작 과정 요약
1. **Ticker 생성**:  
   - `TickerGenerateScheduler`가 1초마다 종목별로 랜덤 시세 생성 → Redis에 저장.
2. **API 제공**:  
   - `/api/v1/stocks/{symbol}/tickers` 요청 시 Redis에서 해당 종목의 최신/지정 시점 데이터를 반환.
3. **도메인/유틸**:  
   - `Ticker`는 심볼, 가격, 변동률, 거래량, 갱신시각을 보유.
   - `DataSerializer`로 JSON 직렬화/역직렬화.

---

### 예시 API

```http
GET /api/v1/stocks/AAPL/tickers
```
- 응답: AAPL(Apple) 가상 실시간 시세 JSON

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

- 본 프로젝트는 실제 투자용이 아닌, 테스트/데모/개발 환경용 가상 주식 시세 데이터 시뮬레이터입니다.

