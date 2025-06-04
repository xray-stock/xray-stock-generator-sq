package app.xray.stock.stock_generator.adapter.in.job;

import app.xray.stock.stock_generator.application.port.in.GenerateTickerUseCase;
import app.xray.stock.stock_generator.application.port.out.SaveTickDataPort;
import app.xray.stock.stock_generator.domain.Ticker;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class TickerGenerateScheduler {

    private final GenerateTickerUseCase tickerGenerator;
    private final SaveTickDataPort saveTickDataPort;

    private final List<String> symbols = List.of("AAPL", "TSLA", "MSFT"); // TODO 열거형 정리 또는 별도 파일 입력으로

    public TickerGenerateScheduler(GenerateTickerUseCase tickerGenerator,
                                   SaveTickDataPort saveTickDataPort) {
        this.tickerGenerator = tickerGenerator;
        this.saveTickDataPort = saveTickDataPort;
    }

    @Scheduled(fixedRate = 1000)
    public void generate() {
        for (String symbol : symbols) { // TODO 대용량 처리하려면?
            Ticker ticker = tickerGenerator.generate(symbol);
            saveTickDataPort.saveTickData(ticker);
        }
        log.info("[TickerGenerateScheduler.generate] saved! total count: {}", symbols.size());
    }
}
