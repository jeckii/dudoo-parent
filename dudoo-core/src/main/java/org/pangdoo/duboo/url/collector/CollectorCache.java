package org.pangdoo.duboo.url.collector;

import java.util.HashMap;
import java.util.Map;

import org.pangdoo.duboo.url.UrlCollector;

public class CollectorCache {
	
	private static Map<String, UrlCollector> cache = new HashMap<String, UrlCollector>();
	
	public static UrlCollector get(String key) {
		return cache.get(key);
	}
	
	public static Map<String, UrlCollector> put(String key, UrlCollector value) {
		cache.put(key, value);
		return cache;
	}

}
