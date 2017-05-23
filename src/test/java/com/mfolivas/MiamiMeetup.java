package com.mfolivas;

import com.mfolivas.cache.CacheServer;
import com.mfolivas.finance.StockTradingService;
import com.mfolivas.util.Sleeper;
import com.mfolivas.weather.Weather;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.rx.ReactiveCamel;
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
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.fail;

/**
 * @author Marcelo Olivas
 */
@Ignore
public class MiamiMeetup {
    private static final Logger log = LoggerFactory.getLogger(MiamiMeetup.class);
    public static final List<String> STOCKS = Arrays.asList("GOOG", "AMZN", "MSFT", "ORCL", "IBM", "NFLX", "FB");
    public static final String DIRECTORY = "file:///Users/Marcelo/development/personal/rx-legacy/src/test/java/resources";
    //TODO(Marcelo) show journey - both company and presentation

    @Test
    public void should_return_an_iterative_result() {

    }

    @Test
    public void should_return_an_completable_future() {


        /**
         * Unless you can model your entire system synchronously, a single asynchronous source
         * breaks imperative programming. - Jake Wharton.
         */
    }

    @Test
    public void should_return_an_observable() {

    }

    @Test
    public void should_return_all_items_added_by_ten_and_then_multiplied_by_2_and_only_get_the_even_then_add_them() {
    }

    @Test
    public void should_return_the_stocks_from_financial_exchange() {


        //TODO(Marcelo) show completed signal



    }

    @Test
    public void should_return_an_error_if_stock_result_takes_more_than_500_milliseconds() {

    }


    @Test
    public void should_return_the_stocks_with_an_additional_3_percent_commission_and_only_show_stocks_over_100() {

    }

    @Test
    public void should_execute_the_execution_of_the_stock_prices_in_a_multi_threaded_way() {

    }


    @Test
    public void should_create_a_free_stock_trading_service_that_has_intermediate_errors() {

    }

    @Test
    public void should_have_a_fallback_financial_system_for_the_free_system() {

    }

    @Test
    public void should_retry_three_times_to_get_free_stock_quotes() {

    }


    @Test
    public void should_get_the_first_result_from_two_cache() {


    }

    @Test
    public void should_create_two_observables_news_and_weather() {


    }


    @Test
    public void should_return_a_counter_every_second_milliseconds() {

        //TODO(Marcelo) poll for the resource directory
    }

    @Test
    public void should_poll_for_changes_on_the_directory() throws Exception {
        final DefaultCamelContext camel = new DefaultCamelContext();

        final ReactiveCamel rxCamel = new ReactiveCamel(camel);

        //Not
        final rx.Observable<Message> msg = rxCamel
                .toObservable(DIRECTORY);

        msg.subscribe(this::print);

        TimeUnit.SECONDS.sleep(100);

    }

    @Test
    public void should_check_a_reactive_camel_with_jms() throws Exception {
        final DefaultCamelContext camel = new DefaultCamelContext();

        final ReactiveCamel rxCamel = new ReactiveCamel(camel);
        rxCamel
                .toObservable("activemq:queue:meetup")
                .map(Message::getBody)
                .subscribe(this::print);

        TimeUnit.SECONDS.sleep(100);
    }


    void print(Object object) {
        log.info("Got: {}", object);
    }

}
