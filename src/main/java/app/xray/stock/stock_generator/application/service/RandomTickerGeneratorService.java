package app.xray.stock.stock_generator.application.service;

import app.xray.stock.stock_generator.application.port.in.GenerateTickerUseCase;
import app.xray.stock.stock_generator.domain.RandomTickerGenerator;
import app.xray.stock.stock_generator.domain.Ticker;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;

@Service
class RandomTickerGeneratorService implements GenerateTickerUseCase {

    private final SecureRandom random = new SecureRandom();

    @Override
    public Ticker generate(String symbol) {
        RandomTickerGenerator generator = RandomTickerGenerator.builder()
                .random(random)
                .symbol(symbol)
                .build();
        return generator.generate();
    }
}
