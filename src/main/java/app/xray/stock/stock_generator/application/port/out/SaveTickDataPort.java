package app.xray.stock.stock_generator.application.port.out;

import app.xray.stock.stock_generator.domain.Ticker;

public interface SaveTickDataPort {
    void saveTickData(Ticker ticker);
}
