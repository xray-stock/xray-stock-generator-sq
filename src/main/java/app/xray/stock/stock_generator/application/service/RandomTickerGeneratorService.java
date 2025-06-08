package app.xray.stock.stock_generator.application.service;

import app.xray.stock.stock_generator.application.port.in.GenerateTickerUseCase;
import app.xray.stock.stock_generator.domain.RandomTickerV1Generator;
import app.xray.stock.stock_generator.domain.Ticker;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
class RandomTickerGeneratorService implements GenerateTickerUseCase {

    private final SecureRandom random = new SecureRandom();

    @Override
    public Ticker generate(String symbol) {
        RandomTickerV1Generator generator = RandomTickerV1Generator.builder()
                .random(random)
                .symbol(symbol)
                .build();
        return generator.generate();
    }
}
