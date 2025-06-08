package app.xray.stock.stock_generator.adapter.out.persistence;

import app.xray.stock.stock_generator.adapter.out.persistence.support.RedisTickKeyHelper;
import app.xray.stock.stock_generator.adapter.out.persistence.support.RedisTickStreamHelper;
import app.xray.stock.stock_generator.application.port.out.LoadTickDataPort;
import app.xray.stock.stock_generator.common.util.DataSerializer;
import app.xray.stock.stock_generator.domain.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisTickDataQueryRepository implements LoadTickDataPort {

    private static final String FIELD_PAYLOAD = "payload";
    private static final String EARLIEST_ID   = "0-0";

    private final StringRedisTemplate redisTemplate;

    @Override
    public Optional<Ticker> loadTickData(String symbol, Instant at) {
        return findStreamLatestTicker(symbol, at);
    }

    private Optional<Ticker> findValueTicker(String symbol, Instant at) {
        String key = RedisTickKeyHelper.generateKey(symbol, at);
        String json = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(json)) {
            return Optional.empty();
        }
        return Optional.of(DataSerializer.deserialize(json, Ticker.class));
    }

    Optional<Ticker> findStreamLatestTicker(String symbol, Instant at) {
        String key = RedisTickStreamHelper.generateStreamKey(symbol);
        String endId = RedisTickStreamHelper.toStreamId(at);

        // 최신 하나만 뒤에서부터 꺼낸다
        List<MapRecord<String, String, String>> records = redisTemplate.<String,String>opsForStream()
                .reverseRange(key, Range.closed(EARLIEST_ID, endId), Limit.limit().count(1));

        if (Objects.isNull(records) || records.isEmpty()) {
            return Optional.empty();
        }

        String json = records.getFirst()
                .getValue()
                .get(FIELD_PAYLOAD);

        return Optional.of(DataSerializer.deserialize(json, Ticker.class));
    }
}

