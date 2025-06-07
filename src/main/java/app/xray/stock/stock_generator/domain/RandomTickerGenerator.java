package app.xray.stock.stock_generator.domain;

import lombok.Builder;
import org.springframework.util.Assert;

import java.security.SecureRandom;
import java.time.Instant;

public class RandomTickerGenerator {

    private final SecureRandom random;
    private final String symbol;

    @Builder
    private RandomTickerGenerator(SecureRandom random, String symbol) {
        Assert.notNull(random, "random must not be null.");
        Assert.hasText(symbol, "symbol must not be blank.");
        this.random = random;
        this.symbol = symbol;
    }

    public Ticker generate() {
        double base = 100.0;
        double fluctuation = (random.nextDouble() - 0.5) * 2.0;
        double price = Math.round((base + base * fluctuation / 100) * 100.0) / 100.0;
        double changeRate = fluctuation;
        long volume = random.nextInt(10000) + 1;

        return new Ticker(symbol, price, changeRate, volume, Instant.now());
    }
}
