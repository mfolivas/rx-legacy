package com.mfolivas;

import com.mfolivas.finance.StockInfo;
import com.mfolivas.finance.StockTradingService;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public class Exercises {

    @Test
    public void testShouldGetFinancialInformation() {
        final List<String> symbols = Arrays.asList("AMD", "HPQ", "IBM", "TXN", "VMW", "XRX", "AAPL", "ADBE",
                "AMZN", "CRAY", "CSCO", "SNE", "GOOG", "INTC", "INTU", "MSFT", "ORCL", "TIBX", "VRSN", "YHOO");
        Observable.<StockInfo>create(emitter ->
                emitStock(emitter, symbols)).subscribe(
                System.out::println,
                System.out::println,
                () -> System.out.println("DONE"));
    }

    private static void emitStock(ObservableEmitter<StockInfo> emitter, List<String> symbols) {
        symbols.stream().map(symbol -> new StockInfo(symbol, StockTradingService.getPrice(symbol))).forEach(emitter::onNext);
        emitter.onComplete();
        emitter.onNext(new StockInfo("no name", 0.0));
    }
}
