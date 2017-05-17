package com.mfolivas.dao;

import com.mfolivas.util.Sleeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PersonDao {

	private static final Logger log = LoggerFactory.getLogger(PersonDao.class);

	public Person findById(int id) {
		//SQL, SQL, SQL
		log.info("Loading {}", id);
		Sleeper.sleep(Duration.ofMillis(1000));
		return new Person();
	}

}
