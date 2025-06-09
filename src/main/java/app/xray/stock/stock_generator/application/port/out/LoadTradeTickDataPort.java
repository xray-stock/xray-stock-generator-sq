package app.xray.stock.stock_generator.application.port.out;

import app.xray.stock.stock_generator.domain.TradeTick;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface LoadTradeTickDataPort {
    Optional<TradeTick> loadTradeTick(String symbol, Instant at);

    List<TradeTick> loadTradeTicks(String symbol, Instant from, Instant to, int limit);
}
