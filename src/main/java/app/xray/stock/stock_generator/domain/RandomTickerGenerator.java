package app.xray.stock.stock_generator.domain;

import lombok.Builder;
import org.springframework.util.Assert;

import java.security.SecureRandom;
import java.time.Instant;

/**
 * RandomTickerGenerator는 가상의 주식 시세 데이터를 생성하는 시뮬레이터입니다.
 *
 * [주가 생성 원리]
 * - basePrice(기준가)를 중심으로 ±1.0% 범위 내에서 랜덤한 등락률(fluctuationPercent)을 적용하여 현재가 계산
 * - price = basePrice * (1 + fluctuationPercent / 100.0)
 * - changeRate는 fluctuationPercent 값을 그대로 사용 (기준 대비 변화율 %)
 * - 거래량(volume)은 1 ~ 10,000 사이의 랜덤값으로 생성 (단일 tick에서 발생한 거래량)
 *
 * [주의사항]
 * - 현재 거래량은 누적값이 아니라 1 tick 단위 거래량
 * - basePrice는 현재 고정값으로 사용 중이며, 종목별 적용 가능 (향후 개선 포인트)
 */
public class RandomTickerGenerator {

    private final SecureRandom random;
    private final String symbol;

    @Builder
    private RandomTickerGenerator(SecureRandom random, String symbol) {
        Assert.notNull(random, "random must not be null.");
        Assert.hasText(symbol, "symbol must not be blank.");
        this.random = random;
        this.symbol = symbol;
    }

    /**
     * 단일 Tick 데이터(현재 시점의 Ticker)를 생성합니다.
     *
     * @return 새로 생성된 Ticker 객체
     */
    public Ticker generate() {
        double basePrice = 100.0; // 기준 가격 (예: 전일 종가 또는 임의 시가 기준)

        // -1.0% ~ +1.0% 의 등락률
        double fluctuationPercent = (random.nextDouble() - 0.5) * 2.0;
        // 현재가 계산: 기준가 * (1 + 등락률%)
        double price = basePrice * (1 + fluctuationPercent / 100.0);
        // 등락률(%) 값
        double changeRate = fluctuationPercent;
        // 단일 tick 거래량 (1 ~ 10000 랜덤)
        long volume = random.nextInt(10000) + 1;

        return new Ticker(symbol, price, changeRate, volume, Instant.now());
    }
}
