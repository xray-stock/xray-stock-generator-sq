package app.xray.stock.stock_generator.common.exception;

/**
 * 존재하지 않는 종목(symbol) 요청 시 발생하는 예외입니다.
 * API에서 400 Bad Request로 처리됩니다.
 */
public class NotFoundSymbolException extends RuntimeException {

    public NotFoundSymbolException(String symbol) {
        super(String.format("Not Found Symbol StockTickerType. symbol=%s", symbol));
    }
}
