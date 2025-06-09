package app.xray.stock.stock_generator.domain;

import app.xray.stock.stock_generator.common.util.RoundUtil;
import lombok.Builder;
import org.springframework.util.Assert;

import java.security.SecureRandom;
import java.time.Instant;

/**
 * RandomTickerV2Generator 는 가상의 주식 시세 데이터를 생성하는 시뮬레이터입니다.
 * <pre>
 * [주가 생성 원리]
 * - 입력으로 전달받은 previousPrice(이전 tick의 가격)를 기준으로,
 *   FluctuationPercent 객체가 제공하는 ±N% 범위 내 랜덤 등락률(fluctuationRandomPercent)을 적용하여 현재가를 계산합니다.
 * - price = previousPrice * (1 + fluctuationRandomPercent / 100.0)
 * - changeRate는 fluctuationRandomPercent 값을 그대로 사용 (기준 대비 변화율 %)
 * - 거래량(volume)은 1 ~ volumeMaxLimit 사이의 랜덤값으로 생성 (단일 tick에서 발생한 거래량)
 *
 * [설계 특징]
 * - FluctuationPercent 객체 주입으로 변동폭(Volatility)을 종목별/전략별로 유연하게 설정 가능
 * - volumeMaxLimit 설정을 통해 종목별 최대 거래량 상한값 조정 가능
 * - basePrice는 고정값이 아닌 호출 시 전달되는 previousPrice 기준으로 계산
 *
 * [주의사항]
 * - 현재 거래량은 누적값이 아니라 개별 tick 단위 거래량
 * - 이전 가격(previousPrice)은 외부 호출자가 관리하며, 이를 기반으로 다음 tick 가격을 생성함
 * </pre>
 */
public class RandomTradeTickGenerator {

    private final SecureRandom random;
    /** 종목 심볼 */
    private final String symbol;
    /** 등락률 퍼센트 */
    private final FluctuationPercent fluctuationPercent;
    /** 최대 거래량 상한(1 ~ 끝값 포함) */
    private int volumeMaxLimit = 10000;
    /** 소수점 자리수 */
    private final int decimalPlaces;

    @Builder
    private RandomTradeTickGenerator(
            SecureRandom random,
            String symbol,
            FluctuationPercent fluctuationPercent,
            Integer volumeMaxLimit,
            int decimalPlaces) {
        Assert.notNull(random, "random must not be null.");
        Assert.hasText(symbol, "symbol must not be blank.");
        Assert.notNull(fluctuationPercent, "fluctuationPercent must not be blank.");
        this.random = random;
        this.symbol = symbol;
        this.fluctuationPercent = fluctuationPercent;
        this.decimalPlaces = decimalPlaces;

        if (volumeMaxLimit != null) {
            if (volumeMaxLimit <= 0) {
                throw new IllegalArgumentException("volumeMaxLimit must not be");
            }
            this.volumeMaxLimit = volumeMaxLimit;
        }
    }

    /**
     * 단일 Tick 데이터(현재 시점의 Ticker)를 생성합니다.
     *
     * @param previousPrice 직전 tick 가격 (이 값을 기준으로 현재 tick 가격을 생성)
     * @return 새로 생성된 Ticker 객체
     */
    public TradeTick generate(double previousPrice, Instant at) {

        double fluctuationRandomPercent = fluctuationPercent.generateRandomPercent(random);// ((random.nextDouble() - 0.5) * 2.0);
        // 등락률 적용하여 금액 계산
        double price = previousPrice * (1 + fluctuationRandomPercent / 100.0);
        // 등락률(%) 값
        double changeRate = fluctuationRandomPercent;
        // 단일 tick 거래량 (1 ~ 10000 랜덤)
        long volume = random.nextInt(volumeMaxLimit) + 1;
        // 소수점 처리
        double roundedPrice = RoundUtil.round(price, decimalPlaces);
        return new TradeTick(symbol, roundedPrice, changeRate, volume, at);
    }
}
