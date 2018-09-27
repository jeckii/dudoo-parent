package org.pangdoo.duboo.url;

import org.pangdoo.duboo.exception.NullException;
import org.pangdoo.duboo.util.StringUtils;

public class WebUrl {
	
	public WebUrl() {
	}
	
	public WebUrl(final String url) {
		this(url, 1);
	}
	
	public WebUrl(final String url, int depth) {
		this.originalUrl = url;
		this.depth = depth;
		this.url = UrlResolver.parser(url);
		this.domain = extractDomain(this.url.getLocation());
	}
	
	private String originalUrl;
	
	private String domain;
	
	private int depth;
	
	private Url url;

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}
	
	private String extractDomain(String location) {
		if (StringUtils.isEmpty(location)) {
			throw new NullException("Location is null.");
		}
		String[] s = location.split("\\.");
		int len = s.length;
		if (len == 1) {
			// Abnormal parameter format
			throw new IllegalArgumentException("Parameter format is abnormal");
		}
		if (s[len - 2].equals("com")) {
			return s[len - 3] + "." + s[len - 2] + "." + s[len - 1];
		} else {
			return s[len - 2] + "." + s[len - 1];
		}
	}
	
}
