package com.mfolivas;

import com.mfolivas.finance.StockInfo;
import com.mfolivas.finance.StockTradingService;
import com.mfolivas.finance.FreeStockTradingService;
import com.mfolivas.util.Sleeper;
import com.mfolivas.weather.Weather;

import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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


    @Test
    public void shouldReturnZip() {
        Observable<Integer> obs1 = Observable.just(1, 3, 5, 7, 9);
        Observable<Integer> obs2 = Observable.just(2, 4, 6);

        Observable<List<Integer>> obs = Observable.zip(obs1, obs2, (value1, value2) -> {
            List<Integer> list = new ArrayList<>();
            list.add(value1);
            list.add(value2);

            return list;
        });

        obs.subscribe((value) -> {
            System.out.println("SubscribeValue = " + value);
        });
    }

    @Test
    public void shouldProvideWeatherAndNewHighlights() {
        Observable<Weather> weather = Observable.fromCallable(() -> new Weather()).delay(1, TimeUnit.SECONDS);
        Observable<String> news = Observable.fromCallable(() -> "Traffic jam in I-75");
//        Observable<String> lottoCombo = Observable.fromCallable(() -> "3-1-9-2-8-4");

        Observable<String> newsAndTraffic =
                Observable.zip(news, weather, (traffic, conditions) ->
                        "Current conditions " + traffic +
                                " and now to the weather, it is currently: " + conditions + " in Miami.");

        newsAndTraffic.subscribe(System.out::println);

        Sleeper.sleep(Duration.ofSeconds(1));

    }

}
