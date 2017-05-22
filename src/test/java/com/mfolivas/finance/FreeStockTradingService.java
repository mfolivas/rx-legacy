package com.mfolivas.finance;

import org.apache.commons.lang3.RandomUtils;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author Marcelo Olivas
 * Application throws an error randomly.
 */
public class FreeStockTradingService {

    public static double getPrice(final String ticker) {
        if (RandomUtils.nextDouble(1, 10) < 2) {
            throw new RuntimeException("Error in the trading service!");
        }

        return StockTradingService.getPrice(ticker);
    }

    public static Observable<StockInfo> getStockPrices(List<String> stocks) {
        return Observable.create(subscriber -> {
            stocks.stream()
                    .forEach(ticker -> subscriber.onNext(new StockInfo(ticker, getPrice(ticker))));
        });
    }
}
