package app.xray.stock.stock_generator.application.port.out;

import app.xray.stock.stock_generator.domain.Ticker;

import java.time.Instant;
import java.util.Optional;

public interface LoadTickDataPort {

    Optional<Ticker> loadTickData(String symbol, Instant at);
}
