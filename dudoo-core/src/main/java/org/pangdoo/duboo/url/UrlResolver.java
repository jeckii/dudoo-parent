package org.pangdoo.duboo.url;

import org.pangdoo.duboo.util.StringUtils;

public final class UrlResolver {
	
	public static Url resolver(Url source, String relation) {
		return resolver(source, parser(relation));
	}
	
	public static Url resolver(Url source, Url relation) {
		if (StringUtils.isEmpty(source.getScheme())) {
			source.setScheme(relation.getScheme());
		}
		String sourceLocation = source.getLocation();
		if (StringUtils.isEmpty(sourceLocation)) {
			source.setLocation(relation.getLocation());
		}
		String sourcePath = source.getPath();
		if (sourcePath.startsWith("./")) {
			source.setPath(sourcePath.substring(1));
		}
		return source;
	}
	
	public static Url parser(String spec) {
		Url url = new Url();
		int startIndex = 0;
		int endIndex = spec.length();
		int fragmentIndex = spec.indexOf("#");
		if (fragmentIndex > 0) {
			url.setFragment(spec.substring(fragmentIndex + 1));
			endIndex = fragmentIndex;
		}
		int queryIndex = spec.indexOf("?");
		if (queryIndex > 0) {
			url.setQuery(spec.substring(queryIndex + 1, endIndex));
			endIndex = queryIndex;
		}
		int paramIndex = spec.indexOf(";");
		if (paramIndex > 0) {
			url.setParams(spec.substring(paramIndex + 1, endIndex));
			endIndex = paramIndex;
		}
		int schemeIndex = spec.indexOf("://");
		if (schemeIndex > 0) {
			url.setScheme(spec.substring(startIndex, schemeIndex));
			startIndex = schemeIndex + 3;
		}
		int locationIndex = spec.indexOf("/", startIndex);
		if (locationIndex >= 0) {
			String subSpec = spec.substring(startIndex, locationIndex);
			if (!StringUtils.isEmpty(subSpec) && !subSpec.startsWith(".")) {
				url.setLocation(subSpec);
				startIndex = locationIndex;
			}
			url.setPath(spec.substring(startIndex, endIndex));
		} else {
			url.setLocation(spec.substring(startIndex, endIndex));
		}
		return url;
	}
	
	public static String pathPattern(String path) {
		if (path.indexOf("*") == -1) {
			return path + "\\S+";
		}
		if (path.endsWith("$")) {
			return path.replaceAll("\\*", "\\\\S+");
		}
		return path.replaceAll("\\*", "\\\\w+") + "\\S+";
	}
	
}
