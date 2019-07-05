package org.pangdoo.duboo.url;

import org.pangdoo.duboo.util.StringUtils;

public class WebURL {
	
	public WebURL() {
	}
	
	public WebURL(final String url) {
		this(url, 1);
	}
	
	public WebURL(final String url, int depth) {
		this.origin = url;
		this.depth = depth;
		this.url = URLResolver.parser(url);
		this.domain = extractDomain(getLocation());
	}
	
	private String origin;
	
	private String domain;

	private int depth;
	
	private URL url;

	public String getOrigin() {
		return origin;
	}

	public String getDomain() {
		return domain;
	}

	public String getLocation() {
		if (this.url == null) {
			throw new IllegalURLException("Unable to get URL.");
		}
		return this.url.getLocation();
	}

	public String getScheme() {
		if (this.url == null) {
			throw new IllegalURLException("Unable to get URL.");
		}
		return this.url.getScheme();
	}

	public String getPath() {
		if (this.url == null) {
			throw new IllegalURLException("Unable to get URL.");
		}
		return this.url.getPath();
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getUrl() {
		if (this.url == null) {
			throw new IllegalURLException("Unable to get URL.");
		}
		return url.toString();
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	
	private String extractDomain(String location) {
		if (StringUtils.isEmpty(location)) {
			throw new IllegalURLException("Lack of location.");
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

	@Override
	public int hashCode() {
		if (origin == null) {
			return super.hashCode();
		}
		return origin.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		return obj instanceof WebURL
				&& (( this.origin == ((WebURL) obj).getOrigin())
				|| this.origin.equals(((WebURL) obj).getOrigin()));

	}
}
