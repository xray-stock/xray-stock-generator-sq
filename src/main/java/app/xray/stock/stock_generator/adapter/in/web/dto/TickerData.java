package app.xray.stock.stock_generator.adapter.in.web.dto;

import app.xray.stock.stock_generator.domain.Ticker;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class TickerData {
    private String symbol;
    private Double price;
    private Double changeRate;
    private Long volume;
    private Instant updatedAt;
    private Instant requestAt;

    public static TickerData of(Ticker ticker, Instant requestAt) {
        TickerData data = new TickerData();
        data.symbol = ticker.getSymbol();
        data.price = ticker.getPrice();
        data.changeRate = ticker.getChangeRate();
        data.volume = ticker.getVolume();
        data.updatedAt = ticker.getUpdatedAt();
        data.requestAt = requestAt;
        return data;
    }

    public static TickerData empty(String symbol, Instant requestAt) {
        TickerData data = new TickerData();
        data.symbol = symbol;
        data.requestAt = requestAt;
        return data;
    }
}
