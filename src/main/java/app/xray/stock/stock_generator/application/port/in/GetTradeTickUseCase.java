package app.xray.stock.stock_generator.application.port.in;

import app.xray.stock.stock_generator.application.service.dto.TickDataQuery;
import app.xray.stock.stock_generator.domain.TradeTick;

import java.time.Instant;
import java.util.Optional;

public interface GetTradeTickUseCase {

    Optional<TradeTick> getTickData(TickDataQuery query);

    default Optional<TradeTick> getTickData(String symbol) {
        return getTickData(TickDataQuery.of(symbol, Instant.now()));
    };
}
