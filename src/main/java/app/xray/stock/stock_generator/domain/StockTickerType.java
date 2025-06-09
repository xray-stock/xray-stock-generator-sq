package app.xray.stock.stock_generator.domain;

import app.xray.stock.stock_generator.common.exception.NotFoundSymbolException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

/**
 * StockTicker는 주식 종목별 특성값을 정의하는 enum입니다.
 * <pre>
 * [구성 요소]
 * - MarketType: 시장 구분 (KOSPI, NASDAQ 등)
 * - symbol: 종목 코드 (문자열, ex: "005930", "AAPL")
 * - name: 종목명
 * - getDecimal: 소숫점 자리수 (0이면 정수, 2면 소숫점 둘째 자리까지 사용)
 * - fluctuationPercent: 변동률 범위 설정 (±N% 범위 내 등락률 발생 가능)
 * - volumeMaxLimit: 단일 Tick당 최대 거래량 (1 ~ volumeMaxLimit 사이 랜덤 거래량 생성)
 * - initValue: 초기 가격 값 (ex. 최근 종가) — 시뮬레이터 초기 previousPrice로 사용 가능
 *
 * [설계 특징]
 * - 종목별 모든 시뮬레이터용 주요 파라미터를 Enum으로 통합 관리
 * - 종목 코드(symbol) 기반 조회 지원 (findBySymbol 메서드 제공)
 * - 유지보수 시 각 종목 정보가 enum 상수로 명확히 관리됨
 * </pre>
 */
@Getter
@RequiredArgsConstructor
public enum StockTickerType {

    // 최근 2025-06-06 종가 기준
    A005930(MarketType.KOSPI, "005930", "삼성전자", 0, FluctuationPercent.from(1.0), 10000, 78500.0),
    A000660(MarketType.KOSPI, "000660", "하이닉스", 0, FluctuationPercent.from(1.0), 8000, 182000.0),

    AAPL(MarketType.NASDAQ, "AAPL", "Apple", 2, FluctuationPercent.from(2.0), 15000, 187.52),
    TSLA(MarketType.NASDAQ, "TSLA", "Tesla", 2, FluctuationPercent.from(3.0), 20000, 177.30),
    MSFT(MarketType.NASDAQ, "MSFT", "Microsoft", 2, FluctuationPercent.from(1.5), 12000, 421.90);

    private final MarketType marketType;
    private final String symbol;
    private final String name;
    private final int getDecimal;
    private final FluctuationPercent fluctuationPercent;
    private final int volumeMaxLimit;
    private final double initPrice;

    public static StockTickerType from(String symbol) {
        return StockTickerType.findBySymbol(symbol).orElseThrow(() -> new NotFoundSymbolException(symbol));
    }

    /**
     * symbol(종목 코드) 기준으로 StockTicker enum 값을 조회합니다.
     *
     * @param symbol 조회할 종목 코드
     * @return Optional<StockTicker> (해당 symbol과 일치하는 StockTicker가 있으면 반환, 없으면 빈 Optional)
     */
    public static Optional<StockTickerType> findBySymbol(String symbol) {
        return Arrays.stream(values())
                .filter(ticker -> ticker.getSymbol().equals(symbol))
                .findFirst();
    }

    /**
     * 초기 Ticker 객체를 생성합니다.
     * - changeRate는 0.0
     * - volume은 0
     * - updatedAt은 Instant.now() 기준
     */
    public TradeTick createInitialTicker() {
        return new TradeTick(symbol, initPrice, 0.0, 0L, Instant.now());
    }

    /**
     * MarketType는 시장 구분을 정의하는 enum입니다.
     * <pre>
     * - decimalPlaces: 해당 시장에서 사용하는 기본 소숫점 자리수
     * </pre>
     */
    @Getter
    @RequiredArgsConstructor
    public enum MarketType {
        KOSPI(0),
        NASDAQ(2);

        private final int decimalPlaces;
    }
}
