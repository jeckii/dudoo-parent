package org.pangdoo.duboo.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogLogger {
	
	private Logger logger;
	
	public static <T> LogLogger getLogger(Class<T> clazz) {
		return new LogLogger(clazz);
	}
	
	private <T> LogLogger(Class<T> clazz) {
		logger = Logger.getLogger(clazz.getName());
	}
	
	public void info(String msg) {
		logger.log(Level.INFO, msg);
	}
	
	public void info(Throwable thrown) {
		logger.log(Level.INFO, null, thrown);
	}
	
	public void infop(String msg, Object... params) {
		logger.log(Level.INFO, msg, params);
	}
	
	public void info(String msg, Throwable thrown) {
		logger.log(Level.INFO, msg, thrown);
	}
	
	public void warn(String msg) {
		logger.log(Level.WARNING, msg);
	}
	
	public void warn(Throwable thrown) {
		logger.log(Level.WARNING, null, thrown);
	}
	
	public void warn(String msg, Throwable thrown) {
		logger.log(Level.WARNING, msg, thrown);
	}

}
