package app.xray.stock.stock_generator.adapter.out.simulator;

import app.xray.stock.stock_generator.application.port.out.LoadTradeTickDataPort;
import app.xray.stock.stock_generator.domain.RandomTradeTickGenerator;
import app.xray.stock.stock_generator.domain.StockTickerType;
import app.xray.stock.stock_generator.domain.TradeTick;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class RandomTradeTickGenerateQueryRepository implements LoadTradeTickDataPort {

    private final SecureRandom random = new SecureRandom();

    @Override
    public Optional<TradeTick> loadTradeTick(String symbol, Instant at) {
        StockTickerType tickerType = StockTickerType.from(symbol);
        RandomTradeTickGenerator tickerGenerator =  RandomTradeTickGenerator.builder()
                .random(random)
                .symbol(tickerType.getSymbol())
                .fluctuationPercent(tickerType.getFluctuationPercent())
                .volumeMaxLimit(tickerType.getVolumeMaxLimit())
                .build();
        return Optional.of(tickerGenerator.generate(tickerType.getInitPrice()));
    }
}
