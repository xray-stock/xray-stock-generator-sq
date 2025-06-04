package app.xray.stock.stock_generator.adapter.out.persistence;

import app.xray.stock.stock_generator.adapter.out.persistence.support.RedisTickKeyHelper;
import app.xray.stock.stock_generator.application.port.out.SaveTickDataPort;
import app.xray.stock.stock_generator.common.util.DataSerializer;
import app.xray.stock.stock_generator.domain.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisTickDataCommandRepository implements SaveTickDataPort {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void saveTickData(Ticker ticker) {
        String key = RedisTickKeyHelper.generateKey(ticker.getSymbol(), ticker.getUpdatedAt());
        String json = DataSerializer.serialize(ticker);
        redisTemplate.opsForValue().set(key, json, RedisTickKeyHelper.DEFAULT_TTL);
    }
}
