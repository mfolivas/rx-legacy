package com.mfolivas;

import com.mfolivas.cache.CacheServer;
import com.mfolivas.finance.StockTradingService;
import com.mfolivas.util.Sleeper;
import com.mfolivas.weather.Weather;
import com.mfolivas.weather.WeatherClient;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.rx.ReactiveCamel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
/**
 * @author Marcelo Olivas
 */
public class JetConfTest {

    private static final Logger log = LoggerFactory.getLogger(JetConfTest.class);

    @Test
    public void jet_10() throws Exception {
        final CompletableFuture<String> fut =
                CompletableFuture.completedFuture("42");


        final CompletableFuture<Double> di = fut
                .thenApply((String result) -> result.length())
                .thenApply(x -> x * 2.0);
    }

    @Test
    public void sample_25() throws Exception {
        //CompletableFutures but with multiple values
        final Observable<Integer> numbers =
                Observable.just(1, 2, 3);

        numbers.subscribe(this::print);
    }

    void print(Object obj) {
        log.info("Got: {}", obj);
    }

    WeatherClient weatherClient = new WeatherClient();

    @Test
    public void jet_42() throws Exception {
        final Weather minsk = weatherClient.fetch("Minsk");
    }

    @Test
    public void jet_50() throws Exception {
        final Observable<Weather> minsk = weatherClient.rxFetch("Minsk");
        minsk.subscribe(this::print);
    }

    @Test
    public void jet_58() throws Exception {
        final Observable<Weather> minsk = weatherClient.rxFetch("Minsk");
        minsk
                .timeout(1000, TimeUnit.MILLISECONDS)
                .subscribe(this::print);
    }

    @Test
    public void jet_67() throws Exception {
        CacheServer cacheEu = new CacheServer();
        CacheServer cacheUs = new CacheServer();

        final Observable<String> euResult = cacheEu.rxFindBy(42);
        final Observable<String> usResult = cacheUs.rxFindBy(42);

        final Observable<String> allResults = euResult.mergeWith(usResult); //2 strings


        allResults
                .first()
                .observeOn(Schedulers.computation())
                .subscribe(this::print);

        Sleeper.sleep(Duration.ofSeconds(1));
    }

    @Test
    public void jet_94() throws Exception {
        //The reason we need to add the "toBlocking()" is because
        //the application is executing in a different thread.
        Observable
                .interval(1, TimeUnit.SECONDS)
                .take(5)
                .toBlocking()
                .subscribe(this::print);
    }

    List<String> childrenOf(File dir) {
        final File[] array = dir.listFiles();
        final List<File> files = Arrays.asList(array);
        return files
                .stream()
                .map(File::getName)
                .collect(toList());
    }

    Observable<String> rxChildrenOf(File dir) {
        return Observable
                .from(dir.listFiles())
                .map(File::getName);
    }

    final File parent = new File("/Users/Marcelo/development/personal/rx-legacy");

    @Test
    public void jet_118() throws Exception {
        System.out.println(childrenOf(parent));
    }

    @Test
    public void jet_124() throws Exception {
        Observable
                .interval(1_000, TimeUnit.MILLISECONDS)
//				.flatMap(x -> Observable.from(childrenOf(parent)))
                .flatMap(x -> rxChildrenOf(parent))
                .distinct() //danger: memory leak
                //In RxJava 2.0 overloaded distinct()
                .toBlocking()
                .subscribe(this::print);
    }

    @Test
    public void jet_143() throws Exception {
        final DefaultCamelContext camel = new DefaultCamelContext();
        new ReactiveCamel(camel)
                .toObservable("file:/home/tomek/tmp/jetconf")
                .toBlocking()
                .subscribe(this::print);
    }

    @Test
    public void jet_154() throws Exception {
        final DefaultCamelContext camel = new DefaultCamelContext();
        new ReactiveCamel(camel)
//				.toObservable("file:queue:jetconf")
//				.toObservable("kafka:queue:jetconf")
//				.toObservable("imap:queue:jetconf")
                .toObservable("activemq:queue:jetconf")
                .map(Message::getBody)
                .toBlocking()
                .subscribe(this::print);
    }

    @Test
    public void jet_168() throws Exception {
        Observable
                .interval(150, TimeUnit.MILLISECONDS)
                .buffer(1, TimeUnit.SECONDS)
                .toBlocking()
                .subscribe(this::print);
    }

    //Awaitility await()...
    //sleep()
    @Test
    public void jet_177() throws Exception {
        Observable<Long> soap = verySlowSoapService();

        TestScheduler testSched = Schedulers.test();
        TestSubscriber<Long> testSub = new TestSubscriber<>();
        soap
                .timeout(2, TimeUnit.SECONDS, testSched)
                .doOnError(ex -> log.warn("Opps " + ex))
                .retry(4)
                .onErrorReturn(ex -> -1L)
                .subscribe(testSub);

        testSub.assertNoValues();
        testSub.assertNoErrors();

        testSched.advanceTimeBy(9999, TimeUnit.MILLISECONDS);

        testSub.assertNoValues();
        testSub.assertNoErrors();

        testSched.advanceTimeBy(1, TimeUnit.MILLISECONDS);

        testSub.assertNoErrors();
        testSub.assertValue(-1L);

    }

    Observable<Long> verySlowSoapService() {
        return Observable
                .just(Long.valueOf(10))
                .delay(1, TimeUnit.MINUTES);
    }

    @Test
    public void jet_205() throws Exception {
        Observable
                .just(1)
                .subscribeOn(Schedulers.from(Executors.newFixedThreadPool(10)));
    }


}
