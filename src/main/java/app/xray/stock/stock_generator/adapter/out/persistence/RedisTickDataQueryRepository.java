package app.xray.stock.stock_generator.adapter.out.persistence;

import app.xray.stock.stock_generator.application.port.out.LoadTickDataPort;
import app.xray.stock.stock_generator.common.util.DataSerializer;
import app.xray.stock.stock_generator.domain.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisTickDataQueryRepository implements LoadTickDataPort {

    private static final DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final Duration TTL = Duration.ofDays(7);

    private final StringRedisTemplate redisTemplate;

    @Override
    public Optional<Ticker> loadTickData(String symbol, Instant at) {
        String json = redisTemplate.opsForValue().get(generateKey(symbol, at));
        if (Objects.isNull(json)) {
            return Optional.empty();
        }
        return Optional.of(DataSerializer.deserialize(json, Ticker.class));
    }

    private static String generateKey(String symbol, Instant at) {
        return "xray::stock::%s::%s".formatted(symbol, utcFormatter.format(at.atZone(ZoneId.of("UTC"))));
    }
}

