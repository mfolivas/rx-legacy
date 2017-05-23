package com.mfolivas.cache;

import com.mfolivas.util.Sleeper;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Scheduler;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.time.Duration;

public class CacheServer {

	private static final Logger log = LoggerFactory.getLogger(CacheServer.class);

	public String findBy(long key) {
		log.info("Loading from Redis: {}", key);
		Sleeper.sleep(Duration.ofMillis(RandomUtils.nextInt(100, 200)));
		log.info("Cache hit for: {}", key);
		return "<data>" + key + "</data>";
	}

	public Observable<String> rxFindBy(long key) {
		return Observable.fromCallable(() -> findBy(key));
	}

}
