package app.xray.stock.stock_generator.adapter.out.persistence;

import app.xray.stock.stock_generator.adapter.out.persistence.support.RedisTickStreamHelper;
import app.xray.stock.stock_generator.application.port.out.SaveTradeTickDataPort;
import app.xray.stock.stock_generator.common.util.DataSerializer;
import app.xray.stock.stock_generator.domain.TradeTick;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.RedisStreamCommands.XAddOptions;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;

@Log4j2
@Repository
@RequiredArgsConstructor
public class RedisTradeTickCommandRepository implements SaveTradeTickDataPort {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void saveTradeTick(TradeTick tradeTick) {
        saveStream(tradeTick);
    }

    public void saveStream(TradeTick tradeTick) {
        String key = RedisTickStreamHelper.generateStreamKey(tradeTick.getSymbol());
        String json = DataSerializer.serialize(tradeTick);

        try {
            XAddOptions options = XAddOptions.maxlen(10000).approximateTrimming(true);

            RecordId recordId =
                    redisTemplate.opsForStream().add(StreamRecords.newRecord().in(key).ofMap(Collections.singletonMap(
                            "payload", json)), options);

            log.debug("[RedisTickDataCommandRepository.saveStream] Saved tick to stream={}, id={}, ticker={}", key,
                    recordId.getValue(), tradeTick);
        } catch (Exception e) {
            log.error("[RedisTickDataCommandRepository.saveStream] Failed to save tick to stream {}", key, e);
            throw e;
        }
    }
}
