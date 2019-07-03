package org.pangdoo.duboo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogLogger {
	
	private Logger logger;
	
	public static LogLogger getLogger(Class<?> clazz) {
		return new LogLogger(clazz);
	}
	
	private LogLogger(Class<?> clazz) {
		this.logger = LoggerFactory.getLogger(clazz);
	}
	
	public void info(String info){
		this.logger.info(info);
	}

	public void info(String info, Object... objects) {
		this.logger.info(info, objects);
	}
	
	public void info(Exception e){
		this.logger.info(null, e);
	}
	
	public void info(String info, Exception e){
		this.logger.info(info, e);
	}
	
	public void debug(String debug){
		this.logger.debug(debug);
	}

	public void debug(String debug, Object... objects) {
		this.logger.debug(debug, objects);
	}
	
	public void debug(Exception e){
		this.logger.debug(null, e);
	}
	
	public void debug(String debug, Exception e){
		this.logger.debug(debug, e);
	}
	
	public void warn(String warn){
		this.logger.warn(warn);
	}

	public void warn(String warn, Object... objects) {
		this.logger.warn(warn, objects);
	}
	
	public void warn(Exception e){
		this.logger.warn(null, e);
	}
	
	public void warn(String warn, Exception e){
		this.logger.warn(warn, e);
	}
	
	public void error(String error){
		this.logger.error(error);
	}

	public void error(String error, Object... objects) {
		this.logger.error(error, objects);
	}
	
	public void error(Exception e){
		this.logger.error(null, e);
	}
	
	public  void error(String error, Exception e){
		this.logger.error(error, e);
	}

}
