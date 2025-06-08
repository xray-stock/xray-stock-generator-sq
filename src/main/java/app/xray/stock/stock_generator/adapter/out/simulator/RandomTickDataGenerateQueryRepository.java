package app.xray.stock.stock_generator.adapter.out.simulator;

import app.xray.stock.stock_generator.application.port.out.LoadTickDataPort;
import app.xray.stock.stock_generator.domain.RandomTickerV1Generator;
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
        RandomTickerV1Generator tickerGenerator =  RandomTickerV1Generator.builder()
                .random(random)
                .symbol(symbol)
                .build();
        return Optional.of(tickerGenerator.generate());
    }
}
