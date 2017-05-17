package com.mfolivas.ping;

import static com.mfolivas.ping.Status.DOWN;
import static com.mfolivas.ping.Status.UP;

public class Pinger {

	public Status healthy() {
		return Math.random() < 0.9 ? UP : DOWN;
	}

}

