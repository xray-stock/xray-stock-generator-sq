package app.xray.stock.stock_generator.adapter.out.persistence;

import app.xray.stock.stock_generator.application.port.out.SaveTickDataPort;
import app.xray.stock.stock_generator.common.util.DataSerializer;
import app.xray.stock.stock_generator.domain.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Repository
@RequiredArgsConstructor
public class RedisTickDataCommandRepository implements SaveTickDataPort {

    private static final DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Duration TTL = Duration.ofDays(7);
    private final StringRedisTemplate redisTemplate;

    @Override
    public void saveTickData(Ticker ticker) {
        String key = generateKey(ticker.getSymbol(), ticker.getUpdatedAt());
        String json = DataSerializer.serialize(ticker);
        redisTemplate.opsForValue().set(key, json);
    }

    private static String generateKey(String symbol, Instant at) {
        return "xray::stock::%s::%s".formatted(symbol, utcFormatter.format(at.atZone(ZoneId.of("UTC"))), TTL);
    }
}
