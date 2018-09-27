package org.pangdoo.duboo.robots;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.exception.NullException;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.FetcherBuilder;
import org.pangdoo.duboo.fetcher.HttpResponse;
import org.pangdoo.duboo.request.impl.BasicHttpGet;
import org.pangdoo.duboo.url.WebUrl;
import org.pangdoo.duboo.util.LogLogger;

public class RobotstxtFecher {
	
	private LogLogger logger = LogLogger.getLogger(RobotstxtFecher.class);
	
	private final String ROBOTS_TXT_PATH = "/robots.txt";
	
	private final String ALLOW_ITEM = "allow";
	
	private final String DISALLOW_ITEM = "disallow";
	
	private Configuration config;
	
	private Fetcher fetcher;
	
	private Map<String, List<String>> items;
	
	public RobotstxtFecher(Configuration config) {
		this.config = config;
		this.fetcher = FetcherBuilder.build(config);
	}
	
	public void fetch(String location) {
		try {
			if (location == null) {
				throw new NullException("The location is null.");
			}
			if (RobotsCache.hasLocation(location)) {
				return;
			}
			WebUrl webUrl = new WebUrl(location + ROBOTS_TXT_PATH);
	    	HttpResponse response = fetcher.fetch(new BasicHttpGet(webUrl));
	    	if (response.getStatusLine() != null && response.getStatusLine().getStatusCode() == 200) {
	    		HttpEntity entity = response.getEntity();
	    		if (entity != null) {
	    			RobotstxtParser reader = new RobotstxtParser(entity.getContent(), config.getCharset());
		        	items = reader.items(config.getUserAgent());
		        	Robot robot = new Robot();
		        	robot.setAllow(allow());
		        	robot.setDisallow(disallow());
		        	RobotsCache.put(location, robot);
	    		}
	    	}
		} catch (Exception e) {
			logger.warn(e);
		}
	}
	
	private List<String> allow() {
		if (items != null) {
			List<String> allow = items.get(ALLOW_ITEM);
			if (allow == null) {
				return new ArrayList<String>(0);
			}
			return allow;
		}
		return new ArrayList<String>(0);
	}
	
	private List<String> disallow() {
		if (items != null) {
			List<String> disallow = items.get(DISALLOW_ITEM);
			if (disallow == null) {
				return new ArrayList<String>(0);
			}
			return disallow;
		}
		return new ArrayList<String>(0);
	}
	
	public void shutdown() {
		if (fetcher != null) {
			fetcher.shutdown();
		}
	}
	
}
