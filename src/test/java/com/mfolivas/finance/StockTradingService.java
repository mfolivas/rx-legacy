package com.mfolivas.finance;

/**
 * Fetch information about stocks.
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;


public class StockTradingService {
    public static double getPrice(final String ticker) {
        double minRange = 10;
        double maxRange = 500;
        Random random = new Random();
        double randomValue = minRange + (maxRange - minRange) * random.nextDouble();
        BigDecimal price = BigDecimal.valueOf(randomValue).setScale(4, RoundingMode.HALF_EVEN);
        double amount = price.doubleValue();
        return amount;
    }


    public static Observable<StockInfo> getStockPrices(List<String> tickerSymbols) {
        System.out.println("here: " + tickerSymbols);
        return Observable.create(subscriber -> {
            tickerSymbols.stream()
                    .forEach(symbol -> subscriber.onNext(new StockInfo(symbol, StockTradingService.getPrice(symbol))));
            subscriber.onComplete();
        });
    }
}
