package app.xray.stock.stock_generator.adapter.out.persistence.support;

import java.time.Instant;

public class RedisTickStreamHelper {

    /**
     * 스트림 키: ex) xray:stock:MSFT:stream
     */
    public static String generateStreamKey(String symbol) {
        return "xray:stock:%s:stream".formatted(symbol);
    }

    /**
     * Redis Stream ID 포맷 (ms-sequence)
     * - ms: 밀리초(epoch)
     * - sequence: 동시간 ms 내 순번 (0으로 시작)
     * ex) "171xxx1234567-0"
     */
    public static String toStreamId(Instant at) {
        long ms = at.toEpochMilli();
        return "%d-0".formatted(ms);
    }
}
