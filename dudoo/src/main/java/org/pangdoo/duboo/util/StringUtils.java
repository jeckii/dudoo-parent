package org.pangdoo.duboo.util;

import java.util.regex.Pattern;

public class StringUtils {

	public static boolean isEmpty(String s) {
		if (s == null || s.length() == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isBlank(String s) {
		if (s == null || s.trim().length() == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean pattern(String s, String regex) {
		return Pattern.matches(regex, s);
	}
	
}
