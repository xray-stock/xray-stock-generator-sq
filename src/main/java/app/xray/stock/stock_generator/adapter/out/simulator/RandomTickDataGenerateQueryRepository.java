package app.xray.stock.stock_generator.adapter.out.simulator;

import app.xray.stock.stock_generator.application.port.out.LoadTickDataPort;
import app.xray.stock.stock_generator.domain.Ticker;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class RandomTickDataGenerateQueryRepository implements LoadTickDataPort {

    private final SecureRandom random = new SecureRandom();

    @Override
    public Optional<Ticker> loadTickData(String symbol, Instant at) {

        double base = 100.0;
        double fluctuation = (random.nextDouble() - 0.5) * 2.0; // ±1.0%
        double price = Math.round((base + base * fluctuation / 100) * 100.0) / 100.0;
        double changeRate = fluctuation;
        long volume = random.nextInt(10_000) + 1;

        return Optional.of(new Ticker(symbol, price, changeRate, volume, at));
    }
}
