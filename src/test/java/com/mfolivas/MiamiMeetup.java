package com.mfolivas;

import com.mfolivas.cache.CacheServer;
import com.mfolivas.finance.FreeStockTradingService;
import com.mfolivas.finance.Stock;
import com.mfolivas.finance.StockTradingService;
import com.mfolivas.util.Sleeper;
import com.mfolivas.weather.Weather;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.fail;

/**
 * @author Marcelo Olivas
 */
@Ignore
public class MiamiMeetup {
    private static final Logger log = LoggerFactory.getLogger(MiamiMeetup.class);
    public static final List<String> STOCKS = Arrays.asList("GOOG", "AMZN", "MSFT", "ORCL", "IBM", "NFLX", "FB");
    //TODO(Marcelo) show journey - both company and presentation

    @Test
    public void should_return_an_iterative_result() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
//        for (int i = 0; i < numbers.size() ; i++) {
//            System.out.println(numbers.get(i));
//        }

//        Iterator<Integer> itr = numbers.iterator();
//        while (itr.hasNext()) {
//            System.out.println(itr.next());
//        }


        /**
         * Functional programming: 1) immutability 2) treat functions as first class citizens
         * Venkat Subramaniam - benefits:
         * - functional pipeline
         * - lazy evaluation
         */
        numbers.stream()
                .map(number -> number * 2)
                .filter(number -> number % 2 == 0)
                .forEach(System.out::println);

        //Consumer is in control
        //Observer: listeners

    }

    @Test
    public void should_return_an_completable_future() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.completedFuture("1");
        CompletableFuture<Integer> result = future.thenApply((String number) -> number.length()).thenApply((Integer length) -> length * 2);
        System.out.println(result.get());
        /**
         * Unless you can model your entire system synchronously, a single asynchronous source
         * breaks imperative programming. - Jake Wharton.
         */
    }

    @Test
    public void should_return_an_observable() {
        Observable<Integer> numbers = Observable.just(1, 2, 3, 4, 5, 6, 7, 8);
        numbers.subscribe(System.out::print);

    }


    @Test
    public void should_return_the_first_twenty_numbers_multiplied_by_two_and_get_the_sum() {
        Observable<Integer> numbers = Observable.just(1, 2, 3, 4, 5, 6, 7, 8);
        numbers.take(5)
                .map(number -> number * 2)
                .reduce((x, y) -> x + y)
                .subscribe(System.out::println);
    }

    @Test
    public void should_return_the_stocks_from_financial_exchange() {
        //        System.out.println(StockTradingService.getPrice("FB"));

        /*Observable<Stock> stockObservable = Observable.create(new ObservableOnSubscribe<Stock>() {
            @Override
            public void subscribe(ObservableEmitter<Stock> emitter) throws Exception {
                stocks.stream()
                        .map(ticker -> new Stock(ticker, StockTradingService.getPrice(ticker)))
                        .forEach(stock -> emitter.onNext(stock));
                emitter.onComplete();
            }
        });*/
        Observable<Object> stockObservable = Observable.create(emitter -> {
            STOCKS.stream()
                    .map(ticker -> new Stock(ticker, StockTradingService.getPrice(ticker)))
                    .forEach(stock -> emitter.onNext(stock));
            emitter.onComplete();
        });

        stockObservable.subscribe(this::print);


    }

    @Test
    public void should_return_an_error_if_result_takes_more_than_500_milliseconds() {
        Observable<Object> stockObservable = Observable.create(emitter -> {
            STOCKS.stream()
                    .map(ticker -> new Stock(ticker, StockTradingService.getPrice(ticker)))
                    .forEach(stock -> emitter.onNext(stock));
            emitter.onComplete();
        });

        stockObservable
                .timeout(500, TimeUnit.MILLISECONDS)
                .subscribe(this::print);
    }

    @Test
    public void should_return_the_stocks_with_an_additional_3_percent_commission_and_only_show_stocks_over_100() {
        Observable<Stock> portfolio = StockTradingService.rxGetPrice(STOCKS);
        portfolio
//                .map(stock -> new Stock(stock.getSymbol(), commission(stock)))
//                .filter(stock -> stock.getPrice() > 100)
                .subscribe(this::print);
        Sleeper.sleep(Duration.ofSeconds(3));
    }

    @Test
    public void should_execute_the_execution_of_the_stock_prices_in_a_multi_threaded_way() {
        Observable<Stock> portfolio = StockTradingService.rxGetPrice(STOCKS);
        portfolio
                .map(stock -> new Stock(stock.getSymbol(), commission(stock)))
                .subscribe(this::print);
        Sleeper.sleep(Duration.ofSeconds(3));
    }

    @Test
    public void should_create_a_free_stock_trading_that_has_intermediate_errors() {
        Observable<Stock> freeService = FreeStockTradingService.getStockPrices(STOCKS);
        freeService.subscribe(
                this::print,
                (Throwable error) -> System.err.println(error.getMessage())
                );
    }

    @Test
    public void should_have_a_fallback_financial_system_for_the_free_system() {
        FreeStockTradingService.getStockPrices(STOCKS)
                .onErrorResumeNext(StockTradingService.rxGetPrice(STOCKS))
        .subscribe(this::print);
        Sleeper.sleep(Duration.ofSeconds(2));

//        willReturnError(42).onErrorResumeNext(isOk(42))
//        .subscribe(this::print);
    }

    @Test
    public void should_retry_three_times_to_get_free_stock_quotes() {
        FreeStockTradingService.getStockPrices(STOCKS)
                .retry(3)
                .subscribe(this::print, this::print);
    }

    public Observable<Integer> willReturnError(Integer number) {
        return Observable.create(sub -> {
            if (true) {
                System.out.println("Error");
                throw new RuntimeException("oh oh!");
            }
            sub.onNext(number);
        });
    }

    public Observable<Integer> isOk(Integer number) {
        return Observable.fromCallable(() -> number);
    }



    public static Double commission(Stock stock) {
        return stock.getPrice() - (stock.getPrice() * 0.03);
    }

    @Test
    public void should_get_the_first_result_from_two_cache() {
        CacheServer eastCoastRedis = new CacheServer();
        CacheServer westCoastRedis = new CacheServer();

        Observable<String> eastResult = eastCoastRedis.rxFindBy(42);
        Observable<String> westResult = westCoastRedis.rxFindBy(42);

        Observable<String> bothResults = eastResult.mergeWith(westResult);
        bothResults
                .firstElement()
                .subscribe(this::print);

    }

    @Test
    public void should_create_two_observables_news_and_weather() {
        Observable<String> news = Observable.fromCallable(() -> "Miami Heat, we get 'em next season!");
        Observable<Weather> weather = Observable.fromCallable(() -> new Weather());
        Observable<String> result = Observable.zip(news, weather, (sports, climate) -> sports + ". The weather tonight is " + climate);
        result.subscribe(this::print);

    }


    @Test
    public void should_return_a_counter_every_500_milliseconds() {
        fail("Not implemented yet");
    }

    @Test
    public void should_poll_for_changes_on_the_directory() {
        fail("Not implemented yet");
    }

    @Test
    public void should_check_a_reactive_camel_with_jms() {
        fail("Not implemented yet");
    }

    @Test
    public void should_check_for_a_slow_connection_and_retry() {
        fail("Not implemented yet");
    }


    void print(Object object) {
        log.info("Got: {}", object);
    }

}
