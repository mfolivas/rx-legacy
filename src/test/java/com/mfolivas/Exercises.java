package com.mfolivas;

import com.mfolivas.finance.StockInfo;
import com.mfolivas.finance.StockTradingService;
import com.mfolivas.finance.FreeStockTradingService;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableEmitter;
import rx.Observable;


public class Exercises {

    final List<String> STOCKS = Arrays.asList("AMD", "HPQ", "IBM", "TXN", "VMW", "XRX", "AAPL", "ADBE",
            "AMZN", "CRAY", "CSCO", "SNE", "GOOG", "INTC", "INTU", "MSFT", "ORCL", "TIBX", "VRSN", "YHOO");

    @Test
    public void testShouldGetFinancialInformation() {
        Observable.create(subscriber -> {
           STOCKS.stream()
                   .forEach(stock -> subscriber.onNext(new StockInfo(stock, StockTradingService.getPrice(stock))));
        }).subscribe(System.out::println);
    }

    @Test
    public void shouldSkipTheFirstThree() {
        /*Observable.create(subs -> {
            STOCKS.stream()
                    .forEach(stock -> subs.onNext(new StockInfo(stock, StockTradingService.getPrice(stock))));
        })
                .skip(3)
                .subscribe(System.out::println);*/
        StockTradingService.getStockPrices(STOCKS)
                .skip(3)
                .subscribe(System.out::println);
    }

    @Test
    public void shouldSkipWhileThePriceIsGreaterThan30() {
        Observable.<StockInfo>create(subscriber -> {
           STOCKS.stream()
                   .forEach(ticker -> subscriber.onNext(new StockInfo(ticker, StockTradingService.getPrice(ticker))));
        }).skipWhile(stock -> stock.getPrice() > 30)
                .subscribe(System.out::println);
    }

    @Test
    public void shouldSkipWhilePriceIsOver100() {
        Observable.<StockInfo>create(subs -> {
            STOCKS.stream()
                    .forEach(ticker -> subs.onNext(new StockInfo(ticker, StockTradingService.getPrice(ticker))));
        })
                .skipWhile(stock -> stock.getPrice() > 100)
                .subscribe(System.out::println);
    }

    @Test
    public void shouldCaptureExceptionWithUnstableTradingService() {
        Observable.<StockInfo>create(subscriber -> {
            STOCKS.stream()
                    .forEach(ticker -> subscriber.onNext(new StockInfo(ticker, FreeStockTradingService.getPrice(ticker))));
        }).subscribe(System.out::println,
                System.out::println);
    }

    @Test
    public void shouldUseTheFreeStockTradingWhenPossibleButIfThereIsAnErrorUseThePremiumOne() {
        FreeStockTradingService.getStockPrices(STOCKS)
                .onErrorResumeNext(StockTradingService.getStockPrices(STOCKS))
        .subscribe(System.out::println);
    }

    @Test
    public void shouldGenerateJustFiveConsecutiveNumbers(){
        Observable.interval(1, TimeUnit.SECONDS)
                .take(5)
                .toBlocking()
                .subscribe(System.out::println);
    }




}
