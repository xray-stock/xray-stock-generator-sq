package app.xray.stock.stock_generator.domain;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
public class Ticker {
    @NotBlank
    private String symbol;       // 예: "AAPL", "005930.KQ" ← 종목코드
    @Positive
    private double price;        // 현재가 (예: 187.52달러)
    @DecimalMin(value = "-50.0")
    @DecimalMax(value = "50.0")
    private double changeRate;   // 전일 종가 대비 등락률 (예: +1.27%)
    @PositiveOrZero
    private long volume;         // 거래량 (오늘 하루 누적 거래량)
    @NotNull
    private Instant updatedAt;   // 이 데이터가 마지막으로 업데이트된 시각

    public Ticker(String symbol, double price, double changeRate, long volume, Instant updatedAt) {
        this.symbol = symbol;
        this.price = price;
        this.changeRate = changeRate;
        this.volume = volume;
        this.updatedAt = updatedAt.truncatedTo(ChronoUnit.SECONDS);
    }

    public Ticker() { }
}

