package org.pangdoo.duboo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogLogger {
	
	private Logger logger;
	
	public static LogLogger getLogger(Class<?> clazz) {
		return new LogLogger(clazz);
	}
	
	private LogLogger(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}
	
	public void info(String info){
		logger.info(info);
	}

	public void info(String info, Object... objects) {
		logger.info(info, objects);
	}
	
	public void info(Exception e){
		logger.info(null, e);
	}
	
	public void info(String info, Exception e){
		logger.info(info, e);
	}
	
	public void debug(String debug){
		logger.debug(debug);
	}

	public void debug(String debug, Object... objects) {
		logger.debug(debug, objects);
	}
	
	public void debug(Exception e){
		logger.debug(null, e);
	}
	
	public void debug(String debug, Exception e){
		logger.debug(debug, e);
	}
	
	public void warn(String warn){
		logger.warn(warn);
	}

	public void warn(String warn, Object... objects) {
		logger.warn(warn, objects);
	}
	
	public void warn(Exception e){
		logger.warn(null, e);
	}
	
	public void warn(String warn, Exception e){
		logger.warn(warn, e);
	}
	
	public void error(String error){
		logger.error(error);
	}

	public void error(String error, Object... objects) {
		logger.error(error, objects);
	}
	
	public void error(Exception e){
		logger.error(null, e);
	}
	
	public  void error(String error, Exception e){
		logger.error(error, e);
	}

}
