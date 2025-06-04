package app.xray.stock.stock_generator.application.service;

import app.xray.stock.stock_generator.application.port.in.GetTickDataUseCase;
import app.xray.stock.stock_generator.application.port.out.LoadTickDataPort;
import app.xray.stock.stock_generator.application.service.dto.TickDataQuery;
import app.xray.stock.stock_generator.domain.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class TickDataQueryService implements GetTickDataUseCase {

    private final LoadTickDataPort randomTickDataGenerateQueryRepository;

    @Override
    public Optional<Ticker> getTickData(TickDataQuery query) {
        return randomTickDataGenerateQueryRepository.loadTickData(query.getSymbol(), query.getAt());
    }
}

