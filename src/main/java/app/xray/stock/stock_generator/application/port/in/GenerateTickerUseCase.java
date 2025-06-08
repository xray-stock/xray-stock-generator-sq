package app.xray.stock.stock_generator.application.port.in;

import app.xray.stock.stock_generator.domain.StockTickerType;
import app.xray.stock.stock_generator.domain.Ticker;

public interface GenerateTickerUseCase {
    Ticker generate(StockTickerType stockTickerType);
}
