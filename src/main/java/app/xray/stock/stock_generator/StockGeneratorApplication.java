package app.xray.stock.stock_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableScheduling
@SpringBootApplication
public class StockGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockGeneratorApplication.class, args);
	}

}
