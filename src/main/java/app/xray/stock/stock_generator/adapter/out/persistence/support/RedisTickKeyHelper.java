package app.xray.stock.stock_generator.adapter.out.persistence.support;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class RedisTickKeyHelper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final ZoneId UTC = ZoneId.of("UTC");

    public static final Duration DEFAULT_TTL = Duration.ofDays(7);

    public static String generateKey(String symbol, Instant at) {
        String timestamp = FORMATTER.format(at.atZone(UTC));
        return "xray::stock::%s::%s".formatted(symbol, timestamp);
    }
}
