package app.xray.stock.stock_generator.adapter.in.web;

import app.xray.stock.stock_generator.adapter.in.web.dto.TradeTickData;
import app.xray.stock.stock_generator.application.port.in.GetTradeTickUseCase;
import app.xray.stock.stock_generator.application.service.dto.TickDataQuery;
import app.xray.stock.stock_generator.domain.TradeTick;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RequestMapping("/api/v1/stocks")
@RestController
@RequiredArgsConstructor
public class StocksController {

    private final GetTradeTickUseCase getTradeTickUseCase;

    @GetMapping("/{symbol}/ticks")
    public TradeTickData read(@PathVariable("symbol") String symbol, @RequestParam(required = false) Instant at) {
        if (at == null) {
            at = Instant.now();
        }
        Optional<TradeTick> tickData = getTradeTickUseCase.getTickData(TickDataQuery.of(symbol, at));
        return tickData.isPresent()
                ? TradeTickData.of(tickData.get(), at)
                : TradeTickData.empty(symbol, at);
    }

}
