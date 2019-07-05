package org.pangdoo.duboo.url.collector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.pangdoo.duboo.url.URLCollector;

public class CollectorCache {
	
	private final static Map<String, URLCollector> cache = new ConcurrentHashMap<String, URLCollector>();
	
	public static URLCollector get(String key) {
		return cache.get(key);
	}
	
	public static Map<String, URLCollector> put(String key, URLCollector value) {
		cache.put(key, value);
		return cache;
	}

}
