package com.mfolivas;

import com.mfolivas.finance.StockInfo;
import com.mfolivas.finance.StockTradingService;
import com.mfolivas.finance.UnstableStockTradingService;

import org.junit.Test;
import org.junit.internal.matchers.ThrowableCauseMatcher;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public class Exercises {

    final List<String> STOCKS = Arrays.asList("AMD", "HPQ", "IBM", "TXN", "VMW", "XRX", "AAPL", "ADBE",
            "AMZN", "CRAY", "CSCO", "SNE", "GOOG", "INTC", "INTU", "MSFT", "ORCL", "TIBX", "VRSN", "YHOO");

    @Test
    public void testShouldGetFinancialInformation() {
        Observable.<StockInfo>create(emitter ->
                emitStock(emitter, STOCKS)).subscribe(
                System.out::println,
                System.out::println,
                () -> System.out.println("DONE"));
    }

    @Test
    public void shouldSkipTheFirstThree() {
        Observable.<StockInfo>create(emitter -> emitStock(emitter, STOCKS))
                .skip(3)
                .forEach(System.out::println);
    }

    @Test
    public void shouldSkipWhileThePriceIsGreaterThan30() {
        Observable.<StockInfo>create(emitter -> emitStock(emitter, STOCKS))
                .takeWhile(symbol -> symbol.getPrice() > 30)
                .forEach(System.out::println);
    }

    @Test
    public void shouldSkipUntilPriceIsOver100() {
        Observable.<StockInfo>create(emitter -> emitStock(emitter, STOCKS))
                .skipWhile(stock -> stock.getPrice() > 100)
                .forEach(System.out::println);
    }

    @Test
    public void shouldCaptureExceptionWithUnstableTradingService() {
        Observable.<StockInfo>create(emitter -> emitFreeStock(emitter, STOCKS))
                .subscribe(
                        System.out::println,
                        System.out::println ,
                        () -> System.out.println("DONE")
                );
    }

    private static void emitFreeStock(ObservableEmitter<StockInfo> emitter, List<String> symbols) {
        symbols.stream()
                .map(stock -> new StockInfo(stock, UnstableStockTradingService.getPrice(stock)))
                .forEach(emitter::onNext);
        emitter.onComplete();
    }

    private static void emitStock(ObservableEmitter<StockInfo> emitter, List<String> symbols) {
        symbols.stream()
                .map(symbol -> new StockInfo(symbol, StockTradingService.getPrice(symbol)))
                .forEach(emitter::onNext);
        emitter.onComplete();
        emitter.onNext(new StockInfo("no name", 0.0));
    }
}
