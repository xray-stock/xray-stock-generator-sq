package app.xray.stock.stock_generator.adapter.in.web;

import app.xray.stock.stock_generator.adapter.in.web.dto.TradeTickData;
import app.xray.stock.stock_generator.application.port.in.GetTradeTickListUseCase;
import app.xray.stock.stock_generator.application.port.in.GetTradeTickUseCase;
import app.xray.stock.stock_generator.application.service.dto.TickDataQuery;
import app.xray.stock.stock_generator.application.service.dto.TickDataRangeQuery;
import app.xray.stock.stock_generator.domain.TradeTick;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/stocks")
@RestController
@RequiredArgsConstructor
public class StocksController {

    private final GetTradeTickUseCase getTradeTickUseCase;
    private final GetTradeTickListUseCase getTradeTickListUseCase;

    @GetMapping("/{symbol}/trade-ticks/at")
    public TradeTickData readAt(@PathVariable("symbol") String symbol, @RequestParam Instant at) {
        TickDataQuery query = TickDataQuery.of(symbol, at);
        return getTradeTickUseCase.getTickData(query).map(tradeTick -> TradeTickData.withRequestAt(tradeTick, at)).orElseGet(() -> TradeTickData.empty(symbol, at));
    }

    @GetMapping("/{symbol}/trade-ticks/range")
    public List<TradeTickData> readRange(@PathVariable("symbol") String symbol,
                                         @RequestParam Instant from,
                                         @RequestParam Instant to) {
        TickDataRangeQuery query = TickDataRangeQuery.of(symbol, from, to);
        return getTradeTickListUseCase.getTickDataList(query).stream().map(TradeTickData::withoutRequestAt).toList();
    }

    @GetMapping("/{symbol}/trade-ticks/latest")
    public TradeTickData readLatest(@PathVariable("symbol") String symbol) {
        TickDataQuery query = TickDataQuery.of(symbol, Instant.now());
        Optional<TradeTick> tickData = getTradeTickUseCase.getTickData(query);
        return tickData.map(tradeTick -> TradeTickData.withRequestAt(tradeTick, query.getAt())).orElseGet(() -> TradeTickData.empty(symbol, query.getAt()));
    }
}
