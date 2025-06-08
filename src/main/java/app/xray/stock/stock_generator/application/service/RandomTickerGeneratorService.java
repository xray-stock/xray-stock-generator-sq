package app.xray.stock.stock_generator.application.service;






import app.xray.stock.stock_generator.application.port.in.GenerateTickerUseCase;
import app.xray.stock.stock_generator.application.service.support.StorePrevTickProvider;
import app.xray.stock.stock_generator.domain.StockTickerType;
import app.xray.stock.stock_generator.domain.RandomTickerV2Generator;
import app.xray.stock.stock_generator.domain.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
class RandomTickerGeneratorService implements GenerateTickerUseCase {

    private final StorePrevTickProvider storePrevTickProvider;
    private final SecureRandom random = new SecureRandom();

    @Override
    public Ticker generate(StockTickerType stockTickerType) {
        // 이전 값 메모리 로딩
        Ticker ticker = storePrevTickProvider.load(stockTickerType.getSymbol())
                .orElseGet(stockTickerType::createInitialTicker);

        // 랜덤 값 생성
        Ticker generated = RandomTickerV2Generator.builder()
                .random(random)
                .symbol(stockTickerType.getSymbol())
                .fluctuationPercent(stockTickerType.getFluctuationPercent())
                .volumeMaxLimit(stockTickerType.getVolumeMaxLimit())
                .build()
                .generate(ticker.getPrice());

        // 메모리 저장 처리
        storePrevTickProvider.save(generated);
        return generated;
    }
}
