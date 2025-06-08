package app.xray.stock.stock_generator.application.port.out;

import app.xray.stock.stock_generator.domain.TradeTick;

public interface SaveTradeTickDataPort {
    void saveTradeTick(TradeTick tradeTick);
}
