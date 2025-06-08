package app.xray.stock.stock_generator.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static java.time.ZoneOffset.UTC;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String getFormatFullDateTimeUTC(Instant instant) {
        return FORMATTER.format(instant.atZone(UTC));
    }

    public static String getFormatFullDateTime(Instant instant) {
        return FORMATTER.format(instant);
    }
}
