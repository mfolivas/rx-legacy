package com.mfolivas.finance;

/**
 * Fetch information about stocks.
 */

import com.mfolivas.util.Sleeper;

import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


public class StockTradingService {
    public static double getPrice(final String ticker) {
        double minRange = 10;
        double maxRange = 500;
        Random random = new Random();
        double randomValue = minRange + (maxRange - minRange) * random.nextDouble();
        BigDecimal price = BigDecimal.valueOf(randomValue).setScale(4, RoundingMode.HALF_EVEN);
        Sleeper.sleep(Duration.ofMillis(RandomUtils.nextInt(200, 900)));
        double amount = price.doubleValue();
        return amount;
    }


    public static Observable<Stock> rxGetPrice(List<String> stocks) {
        System.out.println("Getting stock quotes");
        return Observable.<Stock>create(emitter -> {
            stocks.stream()
                    .map(ticker -> new Stock(ticker, StockTradingService.getPrice(ticker)))
                    .forEach(stock -> emitter.onNext(stock));
            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
    }
}
