package app.xray.stock.stock_generator.application.service;


import app.xray.stock.stock_generator.application.port.in.GenerateTradeTickUseCase;
import app.xray.stock.stock_generator.application.service.support.StorePrevTickProvider;
import app.xray.stock.stock_generator.domain.StockTickerType;
import app.xray.stock.stock_generator.domain.RandomTradeTickGenerator;
import app.xray.stock.stock_generator.domain.TradeTick;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;

@Service
@RequiredArgsConstructor
class GenerateRandomTradeTickService implements GenerateTradeTickUseCase {

    private final StorePrevTickProvider storePrevTickProvider;
    private final SecureRandom random = new SecureRandom();

    @Override
    public TradeTick generate(StockTickerType stockTickerType) {
        // 이전 값 메모리 로딩
        TradeTick tradeTick = storePrevTickProvider.load(stockTickerType.getSymbol())
                .orElseGet(stockTickerType::createInitialTicker);
        Instant now = Instant.now();

        // 랜덤 값 생성
        TradeTick generated = RandomTradeTickGenerator.builder()
                .random(random)
                .symbol(stockTickerType.getSymbol())
                .fluctuationPercent(stockTickerType.getFluctuationPercent())
                .volumeMaxLimit(stockTickerType.getVolumeMaxLimit())
                .decimalPlaces(stockTickerType.getMarketType().getDecimalPlaces())
                .build()
                .generate(tradeTick.getPrice(), now);

        // 메모리 저장 처리
        storePrevTickProvider.save(generated);
        return generated;
    }
}
