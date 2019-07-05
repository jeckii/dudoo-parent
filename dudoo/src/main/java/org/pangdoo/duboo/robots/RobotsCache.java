package org.pangdoo.duboo.robots;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RobotsCache {
	
	private static final Map<String, Robot> cache = new ConcurrentHashMap<String, Robot>();
	
	public static Robot get(String location) {;
		return cache.get(location);
	}
	
	public static Robot put(String location, Robot value) {
		return cache.put(location, value);
	}
	
	public static boolean hasLocation(String location) {
		return cache.containsKey(location);
	}
	
}
