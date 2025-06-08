package app.xray.stock.stock_generator.adapter.out.persistence.support;

import app.xray.stock.stock_generator.common.util.TimeUtil;

import java.time.Duration;
import java.time.Instant;

public class RedisTickKeyHelper {

    public static final Duration DEFAULT_TTL = Duration.ofDays(7);

    public static String generateKey(String symbol, Instant at) {
        String timestamp = TimeUtil.getFormatFullDateTimeUTC(at);
        return "xray::stock::%s::%s".formatted(symbol, timestamp);
    }
}
