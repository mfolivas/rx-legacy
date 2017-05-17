package com.mfolivas.finance;

/**
 * Fetch information about stocks.
 */

import com.mfolivas.util.Sleeper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Random;


public class StockTradingService {
    public static double getPrice(final String ticker) {
        double minRange = 10;
        double maxRange = 500;
        Random random = new Random();
        double randomValue = minRange + (maxRange - minRange) * random.nextDouble();
        BigDecimal price = BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_EVEN);
        double amount = price.doubleValue();
//        Sleeper.sleep(Duration.ofMillis(price.longValue()));
        return amount;
    }
}
