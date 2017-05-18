package com.mfolivas.finance;

import java.util.Random;

/**
 * @author Marcelo Olivas
 * Application throws an error randomly.
 */
public class UnstableStockTradingService {

    public static double getPrice(final String ticker) {
        Random random = new Random();
        if (random.nextDouble() > 0.3) {
            throw new RuntimeException("Error in the trading service!");
        }

        return StockTradingService.getPrice(ticker);
    }
}
