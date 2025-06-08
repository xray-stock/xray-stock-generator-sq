package app.xray.stock.stock_generator.adapter.in.job;

import app.xray.stock.stock_generator.application.port.in.GenerateTickerUseCase;
import app.xray.stock.stock_generator.application.port.out.SaveTickDataPort;
import app.xray.stock.stock_generator.domain.StockTickerType;
import app.xray.stock.stock_generator.domain.Ticker;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Component
public class TickerGenerateScheduler {

    private final GenerateTickerUseCase tickerGenerator;
    private final SaveTickDataPort saveTickDataPort;

    private final List<StockTickerType> stockTickerTypes = Arrays.stream(StockTickerType.values()).toList();

    public TickerGenerateScheduler(GenerateTickerUseCase tickerGenerator,
                                   SaveTickDataPort saveTickDataPort) {
        this.tickerGenerator = tickerGenerator;
        this.saveTickDataPort = saveTickDataPort;
    }

    @Scheduled(fixedRate = 1000)
    public void generate() {
        for (StockTickerType stockTickerType : stockTickerTypes) { // TODO 대용량 처리하려면?
            Ticker ticker = tickerGenerator.generate(stockTickerType);
            saveTickDataPort.saveTickData(ticker);
        }
        log.info("[TickerGenerateScheduler.generate] saved! total count: {}", stockTickerTypes.size());
    }
}
