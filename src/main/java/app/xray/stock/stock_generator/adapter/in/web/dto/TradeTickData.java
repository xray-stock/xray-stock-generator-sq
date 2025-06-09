package app.xray.stock.stock_generator.adapter.in.web.dto;

import app.xray.stock.stock_generator.domain.TradeTick;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class TradeTickData {
    private String symbol;
    private Double price;
    private Double changeRate;
    private Long volume;
    private Instant updatedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Instant requestAt;

    public static TradeTickData withRequestAt(TradeTick tradeTick, Instant requestAt) {
        TradeTickData data = new TradeTickData();
        data.symbol = tradeTick.getSymbol();
        data.price = tradeTick.getPrice();
        data.changeRate = tradeTick.getChangeRate();
        data.volume = tradeTick.getVolume();
        data.updatedAt = tradeTick.getUpdatedAt();
        data.requestAt = requestAt;
        return data;
    }

    public static TradeTickData withoutRequestAt(TradeTick tradeTick) {
        TradeTickData data = new TradeTickData();
        data.symbol = tradeTick.getSymbol();
        data.price = tradeTick.getPrice();
        data.changeRate = tradeTick.getChangeRate();
        data.volume = tradeTick.getVolume();
        data.updatedAt = tradeTick.getUpdatedAt();
        data.requestAt = null;
        return data;
    }

    public static TradeTickData empty(String symbol, Instant requestAt) {
        TradeTickData data = new TradeTickData();
        data.symbol = symbol;
        data.requestAt = requestAt;
        return data;
    }
}
