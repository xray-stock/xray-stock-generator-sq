package app.xray.stock.stock_generator.application.service.support;

import app.xray.stock.stock_generator.domain.Ticker;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StorePrevTickProvider {

    private final ConcurrentHashMap<String, Ticker> prevTickMap = new ConcurrentHashMap<>();

    public void save(Ticker ticker) {
        prevTickMap.put(ticker.getSymbol(), ticker);
    }

    public Optional<Ticker> load(String symbol) {
        return Optional.ofNullable(prevTickMap.get(symbol));
    }

    public void clear() {
        prevTickMap.clear();
    }

    public int size() {
        return prevTickMap.size();
    }
}
