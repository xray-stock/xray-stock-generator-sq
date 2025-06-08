package app.xray.stock.stock_generator.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundUtil {

    /**
     * decimalPlaces 자리수 까지 value 올림처리
     */
    public static double round(double value, int decimalPlaces) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
