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

    @GetMapping("/{symbol}/trade-ticks")
    public TradeTickData readAt(@PathVariable("symbol") String symbol, @RequestParam Instant at) {
        TickDataQuery query = TickDataQuery.of(symbol, at);
        return getTradeTickUseCase.getTickData(query).map(tradeTick -> TradeTickData.of(tradeTick, at)).orElseGet(() -> TradeTickData.empty(symbol, at));
    }

    @GetMapping("/{symbol}/trade-ticks/latest")
    public TradeTickData readLatest(@PathVariable("symbol") String symbol) {
        TickDataQuery query = TickDataQuery.of(symbol, Instant.now());
        Optional<TradeTick> tickData = getTradeTickUseCase.getTickData(query);
        return tickData.map(tradeTick -> TradeTickData.of(tradeTick, query.getAt())).orElseGet(() -> TradeTickData.empty(symbol, query.getAt()));
    }
}
