package app.xray.stock.stock_generator.adapter.out.simulator;

import app.xray.stock.stock_generator.application.port.out.LoadTradeTickDataPort;
import app.xray.stock.stock_generator.domain.RandomTradeTickGenerator;
import app.xray.stock.stock_generator.domain.StockTickerType;
import app.xray.stock.stock_generator.domain.TradeTick;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class RandomTradeTickGenerateQueryRepository implements LoadTradeTickDataPort {

    private final SecureRandom random = new SecureRandom();

    @Override
    public Optional<TradeTick> loadTradeTick(String symbol, Instant at) {
        TradeTick tradeTick = generateTradeTick(symbol, at);
        return Optional.of(tradeTick);
    }

    @Override
    public List<TradeTick> loadTradeTicks(String symbol, Instant from, Instant to, int limit) {
        return List.of(generateTradeTick(symbol, from));
    }

    private TradeTick generateTradeTick(String symbol, Instant at) {
        StockTickerType tickerType = StockTickerType.from(symbol);
        RandomTradeTickGenerator tickerGenerator =  RandomTradeTickGenerator.builder()
                .random(random)
                .symbol(tickerType.getSymbol())
                .fluctuationPercent(tickerType.getFluctuationPercent())
                .volumeMaxLimit(tickerType.getVolumeMaxLimit())
                .build();
        return tickerGenerator.generate(tickerType.getInitPrice(), at);
    }
}
