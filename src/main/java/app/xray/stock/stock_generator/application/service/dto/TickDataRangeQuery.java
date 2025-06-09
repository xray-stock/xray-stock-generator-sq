package app.xray.stock.stock_generator.application.service.dto;

import app.xray.stock.stock_generator.common.validation.SelfValidating;
import app.xray.stock.stock_generator.domain.StockTickerType;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.Instant;

@Getter
public class TickDataRangeQuery extends SelfValidating<TickDataQuery> {

    @NotNull
    private final StockTickerType ticker;
    @NotNull
    private final Instant from;
    @NotNull
    private final Instant to;
    private final int limit = 100_000;

    private TickDataRangeQuery(StockTickerType ticker, Instant from, Instant to) {
        this.ticker = ticker;
        this.from = from;
        this.to = to;
        validateSelf();

        if (!from.isBefore(to)) {
            throw new ValidationException(String.format("Invalid time range: 'from' (%s) must be before 'to' (%s).", from, to));
        }
    }

    public static TickDataRangeQuery of(String symbol, Instant from, Instant to) {
        return new TickDataRangeQuery(StockTickerType.from(symbol), from, to);
    }

    public String getSymbol() {
        return this.ticker.getSymbol();
    }
}
