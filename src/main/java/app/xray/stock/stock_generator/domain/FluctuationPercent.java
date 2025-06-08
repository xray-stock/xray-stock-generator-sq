package app.xray.stock.stock_generator.domain;

import lombok.Getter;

import java.util.Random;

/**
 * FluctuationPercent는 주식 시세 시뮬레이션 시 사용할 등락률(변동폭) 범위를 정의하는 값 객체입니다.
 * <pre>
 * [동작 원리]
 * - percent 필드는 "허용 가능한 최대 등락률(%)"을 의미합니다. (예: 1.0 → ±1.0% 허용)
 * - generateRandomPercent 메서드는 주어진 Random 인스턴스를 사용하여:
 *   -percent ~ +percent 범위 내 랜덤 등락률 값을 생성합니다.
 *   예를 들어 percent = 1.0이면 -1.0% ~ +1.0% 중 하나가 랜덤하게 선택됩니다.
 *
 * [설계 특징]
 * - percent 값은 0.0 ~ 100.0 범위 내로만 허용됩니다. (비정상적인 입력 방지)
 * - 변동폭 설정을 TickerGenerator 에 주입하여 종목별, 전략별, 상황별 유연한 등락률 시뮬레이션이 가능합니다.
 * - Value Object로 설계되어 immutable 하게 사용됩니다.
 *
 * [사용 예시]
 * FluctuationPercent.from(1.0) → ±1% 변동폭
 * FluctuationPercent.from(5.0) → ±5% 변동폭
 *
 * [주의사항]
 * - 내부적으로 generateRandomPercent 는 double [-percent, +percent] 구간의 값을 반환합니다.
 * </pre>
 */

@Getter
final class FluctuationPercent {

    private final double percent;

    private FluctuationPercent(double percent) {
        if (percent < 0 || percent > 100.f) {
            throw new IllegalArgumentException("percent must be 0.0 ~ 100.0");
        }
        this.percent = percent;
    }

    public static FluctuationPercent from(double percent) {
        return new FluctuationPercent(percent);
    }

    /**
     * 등락률 범위 내에서 랜덤한 등락률 값을 생성합니다.
     *
     * @param random 사용할 Random 인스턴스
     * @return [-percent, +percent] 범위 내 랜덤 등락률 값 (%)
     */
    public double generateRandomPercent(Random random) {
        return percent * ((random.nextDouble() - 0.5) * 2.0);
    }

}
