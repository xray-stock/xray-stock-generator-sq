package app.xray.stock.stock_generator.application.service.dto;

import app.xray.stock.stock_generator.common.validation.SelfValidating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
public class TickDataQuery extends SelfValidating<TickDataQuery> {

    @NotBlank
    private final String symbol;
    @NotNull
    private final Instant at;

    public TickDataQuery(String symbol, Instant at) {
        this.symbol = symbol;
        this.at = at;
        validateSelf();
    }

    public static TickDataQuery of(String symbol, Instant at) {
        return new TickDataQuery(symbol, at);
    }

}
