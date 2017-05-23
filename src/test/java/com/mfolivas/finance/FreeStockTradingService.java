package com.mfolivas.finance;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author Marcelo Olivas
 * Application throws an error randomly.
 */
public class FreeStockTradingService {
    private static Logger logger = LoggerFactory.getLogger(FreeStockTradingService.class);

    public static double getPrice(final String ticker) {
        if (RandomUtils.nextDouble(1, 10) < 5) {
            logger.error("An error has occurred in the trading service");
            throw new RuntimeException("Error in the trading service!");
        }

        return StockTradingService.getPrice(ticker);
    }

}
