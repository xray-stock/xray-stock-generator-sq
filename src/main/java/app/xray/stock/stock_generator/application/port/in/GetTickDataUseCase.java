package app.xray.stock.stock_generator.application.port.in;

import app.xray.stock.stock_generator.application.service.dto.TickDataQuery;
import app.xray.stock.stock_generator.domain.Ticker;

import java.time.Instant;
import java.util.Optional;

public interface GetTickDataUseCase {

    Optional<Ticker> getTickData(TickDataQuery query);

    default Optional<Ticker> getTickData(String symbol) {
        return getTickData(TickDataQuery.of(symbol, Instant.now()));
    };
}
