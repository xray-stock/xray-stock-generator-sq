package app.xray.stock.stock_generator.adapter.out.persistence;

import app.xray.stock.stock_generator.adapter.out.persistence.support.RedisTickKeyHelper;
import app.xray.stock.stock_generator.application.port.out.LoadTickDataPort;
import app.xray.stock.stock_generator.common.util.DataSerializer;
import app.xray.stock.stock_generator.domain.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisTickDataQueryRepository implements LoadTickDataPort {

    private final StringRedisTemplate redisTemplate;

    @Override
    public Optional<Ticker> loadTickData(String symbol, Instant at) {
        String key = RedisTickKeyHelper.generateKey(symbol, at);
        String json = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(json)) {
            return Optional.empty();
        }
        return Optional.of(DataSerializer.deserialize(json, Ticker.class));
    }
}

