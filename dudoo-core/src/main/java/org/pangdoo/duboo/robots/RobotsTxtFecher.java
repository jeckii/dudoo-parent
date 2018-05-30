package org.pangdoo.duboo.robots;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pangdoo.duboo.exception.NullValueException;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.HttpResponse;
import org.pangdoo.duboo.handler.reader.TxtReader;
import org.pangdoo.duboo.request.impl.BasicHttpGet;
import org.pangdoo.duboo.util.LogLogger;

public class RobotsTxtFecher {
	
	private LogLogger logger = LogLogger.getLogger(RobotsTxtFecher.class);
	
	private final String ROBOTS_TXT_PATH = "/robots.txt";
	
	private final String ALLOW_ITEM = "allow";
	
	private final String DISALLOW_ITEM = "disallow";
	
	private Configuration config;
	
	private Map<String, List<String>> items;
	
	public RobotsTxtFecher(Configuration config, String location) {
		this.config = config;
		try {
			if (this.config == null) {
				throw new NullValueException("Configuration is null.");
			}
			if (location == null) {
				throw new NullValueException("The location is null.");
			}
    		Fetcher fetcher = new Fetcher(config);
        	HttpResponse response = fetcher.fetch(new BasicHttpGet(location + ROBOTS_TXT_PATH));
        	if (response.getStatusLine().getStatusCode() == 200) {
        		TxtReader reader = new TxtReader(response.getEntity().getContent(), "UTF-8");
            	items = reader.items(config.getUserAgent());
        	}
		} catch (Exception e) {
			logger.warn(e);
		}
	}
	
	public List<String> allow() {
		if (items != null) {
			return items.get(ALLOW_ITEM);
		}
		return new ArrayList<String>(0);
	}
	
	public List<String> disallow() {
		if (items != null) {
			return items.get(DISALLOW_ITEM);
		}
		return new ArrayList<String>(0);
	}
	
}
