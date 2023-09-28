package com.gamesage.store.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerManager {
	private static final Logger logger = LoggerFactory.getLogger(LoggerManager.class);

	public static Logger getLogger() {
		return logger;
	}
}
