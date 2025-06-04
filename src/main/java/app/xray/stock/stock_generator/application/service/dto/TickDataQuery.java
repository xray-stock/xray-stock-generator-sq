package app.xray.stock.stock_generator.application.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
public class TickDataQuery {

    @NotBlank
    private final String symbol;
    @NotNull
    private final Instant at;

    public TickDataQuery(String symbol, Instant at) {
        this.symbol = symbol;
        this.at = at.truncatedTo(ChronoUnit.SECONDS);
    }

    public static TickDataQuery of(String symbol, Instant at) {
        return new TickDataQuery(symbol, at);
    }

}
