package com.mfolivas.finance;

/**
 * @author Marcelo Olivas
 */
import com.mfolivas.util.Sleeper;

import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
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
        Sleeper.sleep(Duration.ofMillis(RandomUtils.nextInt(10, 200)));
        return amount;
    }
}
