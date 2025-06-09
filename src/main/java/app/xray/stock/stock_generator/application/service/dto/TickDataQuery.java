package app.xray.stock.stock_generator.application.service.dto;

import app.xray.stock.stock_generator.common.validation.SelfValidating;
import app.xray.stock.stock_generator.domain.StockTickerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
public class TickDataQuery extends SelfValidating<TickDataQuery> {

    @NotNull
    private final StockTickerType ticker;
    @NotNull
    private final Instant at;

    public TickDataQuery(StockTickerType ticker, Instant at) {
        this.ticker = ticker;
        this.at = at;
        validateSelf();
    }

    public static TickDataQuery of(String symbol, Instant at) {
        return new TickDataQuery(StockTickerType.from(symbol), at);
    }

    public String getSymbol() {
        return this.ticker.getSymbol();
    }
}
