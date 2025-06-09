package app.xray.stock.stock_generator.application.service;

import app.xray.stock.stock_generator.adapter.in.web.dto.TradeTickData;
import app.xray.stock.stock_generator.application.port.in.GetTradeTickListUseCase;
import app.xray.stock.stock_generator.application.port.in.GetTradeTickUseCase;
import app.xray.stock.stock_generator.application.port.out.LoadTradeTickDataPort;
import app.xray.stock.stock_generator.application.service.dto.TickDataQuery;
import app.xray.stock.stock_generator.application.service.dto.TickDataRangeQuery;
import app.xray.stock.stock_generator.domain.TradeTick;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class TradeTickQueryService implements GetTradeTickUseCase, GetTradeTickListUseCase {

    private final LoadTradeTickDataPort redisTradeTickQueryRepository;

    @Override
    public Optional<TradeTick> getTickData(TickDataQuery query) {
        return redisTradeTickQueryRepository.loadTradeTick(query.getSymbol(), query.getAt());
    }

    @Override
    public List<TradeTick> getTickDataList(TickDataRangeQuery query) {
        return redisTradeTickQueryRepository.loadTradeTicks(query.getSymbol(),
                query.getFrom(),
                query.getTo(),
                query.getLimit());
    }
}

