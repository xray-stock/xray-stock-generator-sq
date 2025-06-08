package app.xray.stock.stock_generator.adapter.in.job;

import app.xray.stock.stock_generator.application.port.in.GenerateTradeTickUseCase;
import app.xray.stock.stock_generator.application.port.out.SaveTradeTickDataPort;
import app.xray.stock.stock_generator.domain.StockTickerType;
import app.xray.stock.stock_generator.domain.TradeTick;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Component
public class GenerateTicksScheduler {

    private final GenerateTradeTickUseCase tickerGenerator;
    private final SaveTradeTickDataPort saveTradeTickDataPort;

    private final List<StockTickerType> stockTickerTypes = Arrays.stream(StockTickerType.values()).toList();

    public GenerateTicksScheduler(GenerateTradeTickUseCase tickerGenerator,
                                  SaveTradeTickDataPort saveTradeTickDataPort) {
        this.tickerGenerator = tickerGenerator;
        this.saveTradeTickDataPort = saveTradeTickDataPort;
    }

    @Scheduled(fixedRate = 1000)
    public void generate() {
        for (StockTickerType stockTickerType : stockTickerTypes) { // TODO 대용량 처리하려면?
            TradeTick tradeTick = tickerGenerator.generate(stockTickerType);
            saveTradeTickDataPort.saveTradeTick(tradeTick);
        }
        log.info("[TickerGenerateScheduler.generate] saved! total count: {}", stockTickerTypes.size());
    }
}
