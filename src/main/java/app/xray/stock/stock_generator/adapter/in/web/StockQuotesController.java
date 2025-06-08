package app.xray.stock.stock_generator.adapter.in.web;

import app.xray.stock.stock_generator.adapter.in.web.dto.TickerData;
import app.xray.stock.stock_generator.application.port.in.GetTickDataUseCase;
import app.xray.stock.stock_generator.application.service.dto.TickDataQuery;
import app.xray.stock.stock_generator.domain.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RequestMapping("/api/v1/stocks")
@RestController
@RequiredArgsConstructor
public class StockQuotesController {

    private final GetTickDataUseCase getTickDataUseCase;

    @GetMapping("/{symbol}/tickers")
    public TickerData read(@PathVariable("symbol") String symbol, @RequestParam(required = false) Instant at) {
        if (at == null) {
            at = Instant.now();
        }
        Optional<Ticker> tickData = getTickDataUseCase.getTickData(TickDataQuery.of(symbol, at));
        return tickData.isPresent()
                ? TickerData.of(tickData.get(), at)
                : TickerData.empty(symbol, at);
    }

}
