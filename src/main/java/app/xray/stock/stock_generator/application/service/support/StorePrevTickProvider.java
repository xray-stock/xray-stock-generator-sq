package app.xray.stock.stock_generator.application.service.support;

import app.xray.stock.stock_generator.domain.TradeTick;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StorePrevTickProvider {

    private final ConcurrentHashMap<String, TradeTick> prevTickMap = new ConcurrentHashMap<>();

    public void save(TradeTick tradeTick) {
        prevTickMap.put(tradeTick.getSymbol(), tradeTick);
    }

    public Optional<TradeTick> load(String symbol) {
        return Optional.ofNullable(prevTickMap.get(symbol));
    }

    public void clear() {
        prevTickMap.clear();
    }

    public int size() {
        return prevTickMap.size();
    }
}
