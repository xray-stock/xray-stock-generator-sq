package app.xray.stock.stock_generator.common.advice;

import app.xray.stock.stock_generator.common.exception.NotFoundSymbolException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * GlobalControllerAdvice는 API 전역 예외를 핸들링합니다.
 */
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        body.put("code", "VALIDATION_ERROR");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }


    @ExceptionHandler(NotFoundSymbolException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundSymbolException(NotFoundSymbolException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        body.put("code", "SYMBOL_NOT_FOUND");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
