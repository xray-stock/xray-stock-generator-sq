## 📈 xray-stock-generator-sq

**About:**  
> SQ 의 주식 생성기 구현 :)

---

### 프로젝트 개요

**xray-stock-generator-sq**는 주식(Stock) 시세 데이터를 생성하고, 이를 Redis에 저장 및 조회할 수 있도록 구현된 시뮬레이터/미들웨어 서비스입니다.  
주요 목적은 테스트, 개발, 또는 데모 환경에서 가상의 주식 시세(Ticker)를 자동으로 생성하고, API를 통해 해당 데이터를 제공하는 것입니다.

---

### 핵심 기능

- **가상 주식 시세(Ticker) 생성**  
  - 일정 주기로(AAPL, TSLA, MSFT 등) 랜덤 주식 시세 데이터를 생성합니다.
- **Redis에 시세 데이터 저장 및 조회**  
  - 생성된 데이터를 Redis에 저장하고, 필요시 특정 시점의 데이터를 조회합니다.
- **RESTful API 제공**  
  - HTTP API(`/api/v1/stocks/{symbol}/tickers`)를 통해 시세 데이터 조회를 지원합니다.
- **확장 구조**  
  - 저장/조회 인터페이스(Port, Repository 등) 및 도메인 분리로 확장성과 테스트 용이성을 확보했습니다.

---

### 폴더/클래스 구조 및 주요 흐름

```
src/main/java/app/xray/stock/stock_generator/
├── StockGeneratorApplication.java          # Spring Boot 엔트리포인트
├── domain/
│   └── Ticker.java                        # 종목 시세 도메인 (심볼, 가격, 변동률, 거래량, 갱신시각 등)
├── adapter/
│   ├── in/
│   │   ├── job/TickerGenerateScheduler.java   # 일정 주기로 Ticker 생성 및 저장
│   │   └── web/StockQuotesController.java     # 시세 데이터 REST API
│   ├── out/
│   │   ├── persistence/
│   │   │   ├── RedisTickDataCommandRepository.java # Redis에 데이터 저장
│   │   │   └── RedisTickDataQueryRepository.java   # Redis에서 데이터 조회
│   │   └── simulator/RandomTickDataGenerateQueryRepository.java # 랜덤 데이터 생성(테스트용)
├── common/util/
│   └── DataSerializer.java                 # JSON 직렬화/역직렬화 유틸
├── application/
│   ├── port/in/                            # 유스케이스(입력) 인터페이스
│   ├── port/out/                           # 저장/조회(출력) 인터페이스
│   └── service/
│       └── TickDataQueryService.java       # 시세 데이터 조회 서비스 구현
```

---

### 주요 동작 과정

1. **Ticker 생성**
   - `TickerGenerateScheduler`가 1초마다 지정된 심볼 리스트(AAPL 등)별로 랜덤 시세 데이터를 생성.
   - 생성된 데이터를 `SaveTickDataPort`를 통해 Redis에 저장.

2. **API 제공**
   - `/api/v1/stocks/{symbol}/tickers`로 요청 시, 해당 시점의 시세 데이터를 Redis에서 조회하여 반환.
   - 데이터가 없으면 빈 데이터 응답.

3. **도메인/유틸**
   - `Ticker`는 종목명, 가격, 변동률, 거래량, 갱신시각 등 정보를 담는 도메인 객체.
   - `DataSerializer`는 Jackson 기반의 JSON 직렬화/역직렬화 지원.

---

### 예시 API 사용법

```http
GET /api/v1/stocks/AAPL/tickers
```
- 응답: AAPL의 현재(또는 특정 시점) 가상 주가 정보(JSON)

---

### 실행 방법

1. 자바 & Redis 환경 준비
2. `application.yml`에서 Redis 접속 정보 설정
3. 프로젝트 빌드 및 실행
4. API 호출로 데이터 확인

---

### 기술 스택

- Java 17+
- Spring Boot
- Spring Data Redis
- Lombok
- Jackson

---

### 참고

- 이 프로젝트는 실제 투자 목적이 아닌, 테스트/데모/개발용 주식 시세 데이터 시뮬레이터입니다.
