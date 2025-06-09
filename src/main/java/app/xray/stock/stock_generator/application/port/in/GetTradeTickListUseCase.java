package app.xray.stock.stock_generator.application.port.in;

import app.xray.stock.stock_generator.application.service.dto.TickDataRangeQuery;
import app.xray.stock.stock_generator.domain.TradeTick;

import java.util.List;

public interface GetTradeTickListUseCase {

    List<TradeTick> getTickDataList(TickDataRangeQuery query);
}
