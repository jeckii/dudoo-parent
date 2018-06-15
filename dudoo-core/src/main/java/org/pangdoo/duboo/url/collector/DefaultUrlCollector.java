package org.pangdoo.duboo.url.collector;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pangdoo.duboo.url.UrlCollector;
import org.pangdoo.duboo.url.UrlResolver;
import org.pangdoo.duboo.url.WebUrl;
import org.pangdoo.duboo.util.LogLogger;
import org.pangdoo.duboo.util.StringUtils;
import org.pangdoo.duboo.exception.NullException;
import org.pangdoo.duboo.robots.RobotsCache;

public final class DefaultUrlCollector implements UrlCollector {
	
	private LogLogger logger = LogLogger.getLogger(DefaultUrlCollector.class);
	
	public DefaultUrlCollector() {
	}
	
	public DefaultUrlCollector(Collection<String> urls, int depth) {
		add(urls, depth);
	}
	
	public DefaultUrlCollector(Collection<String> urls) {
		this(urls, 1);
	}
	private Set<String> urlSet;
	private Set<WebUrl> urls;
	
	private WebUrl nextUrl;
	
	private Set<WebUrl> remains;
	
	private Set<String> locations;
	
	private Map<String, WebUrl> redirectUrls;

	private Set<WebUrl> addUrls(Collection<String> urls, int depth) {
		try {
			if (this.urls == null) {
				this.urls = new HashSet<WebUrl>();
			}
			Set<WebUrl> urlTempSet = new HashSet<WebUrl>();
			WebUrl webUrl;
			for(String url : urls) {
				webUrl = new WebUrl(url, depth);
				if (this.locations == null) {
					this.locations = new HashSet<String>();
				}
				String location = webUrl.getUrl().getLocation();
				if (!StringUtils.isBlank(location)) {
					this.locations.add(webUrl.getUrl().getScheme() + "://" + location);
				}
				this.urls.add(webUrl);
				urlTempSet.add(webUrl);
			}
			return urlTempSet;
		} catch (Exception e) {
			logger.warn(e);
		}
		return null;
	}
	
	private WebUrl addUrl(String url, int depth) {
		try {
			if (this.urls == null) {
				this.urls = new HashSet<WebUrl>();
			}
			WebUrl webUrl = new WebUrl(url, depth);
			if (this.locations == null) {
				this.locations = new HashSet<String>();
			}
			String location = webUrl.getUrl().getLocation();
			if (!StringUtils.isBlank(location)) {
				this.locations.add(webUrl.getUrl().getScheme() + "://" + location);
			}
			this.urls.add(webUrl);
			return webUrl;
		} catch (Exception e) {
			logger.warn(e);
		}
		return null;
	}
	
	public WebUrl setNextUrl(WebUrl nextUrl) {
		removeRemains(nextUrl);
		return this.nextUrl = nextUrl;
	}

	public boolean removeRemains(WebUrl url) {
		return this.remains.remove(url);
	}

	@Override
	public Set<WebUrl> add(Collection<String> urls, int depth) {
		if (this.urls != null) {
			return this.urls;
		}
		if (this.remains== null) {
			this.remains = new HashSet<WebUrl>();
		}
		Set<WebUrl> temp = addUrls(urlFilter(urls), depth);
		for (WebUrl url : temp) {
			this.remains.add(url);
		}
		return this.remains;
	}
	
	private Set<String> urlFilter(Collection<String> urls) {
		if (this.urlSet == null) {
			this.urlSet = new HashSet<String>();
		}
		Set<String> rulRtn = new HashSet<String>();
		urls.forEach(url -> {
			if (this.urlSet.add(url)) {
				rulRtn.add(url);
			}
		});
		return rulRtn;
	}

	@Override
	public WebUrl consume() throws NullException {
		if (this.remains == null) {
			return null;
		}
		if (this.remains.size() == 0) {
			return null;
		}
		return setNextUrl(this.remains.iterator().next());
	}

	@Override
	public WebUrl moveToRedirect(final String url) {
		try {
			WebUrl next = this.nextUrl;
			if (next == null) {
				throw new NullException("Next URL is null");
			}
			if (this.redirectUrls == null) {
				this.redirectUrls = new HashMap<String, WebUrl>();
			}
			WebUrl nextUrl = addUrl(url, next.getDepth());
			if (nextUrl != null) {
				this.redirectUrls.put(next.getOriginalUrl(), nextUrl);
				this.urls.remove(next);
				return setNextUrl(nextUrl);
			}
		} catch (Exception e) {
			logger.warn(e);
		}
		return null;
	}
	
	@Override
	public long size() throws NullException {
		if (this.urls == null) {
			throw new NullException("List of URL is null.");
		}
		return this.urls.size();
	}
	
	@Override
	public boolean hasNext() {
		if (this.remains == null) {
			try {
				throw new NullException("Remains is null.");
			} catch (NullException e) {
				logger.warn(e);
			}
		}
		if (this.remains.isEmpty()) {
			return false;
		}
		return true;
	}
	
	@Override
	public Set<String> locations() {
		return this.locations;
	}
	
	@Override
	public boolean remove(WebUrl url) {
		this.urls.remove(url);
		return removeRemains(url);
	}
	
	@Override
	public int filter(String location) {
		if (StringUtils.isBlank(location)) {
			throw new IllegalArgumentException("the domain is null.");
		}
		int count = 0;
		List<String> disallows = RobotsCache.get(location).getDisallow();
		for (String disallow : disallows) {
			for (WebUrl url : urls) {
				if (location.equals(url.getUrl().getLocation()) 
						&& StringUtils.pattern(url.getUrl().getPath(), UrlResolver.pathPattern(disallow))) {
					if(urls.remove(url)) {
						count ++;
					}
				}
			}
		}
		return count;
	}
	
	@Override
	public Set<WebUrl> rebuild() throws NullException {
		if (this.urls == null) {
			throw new NullException("Set of URL is null.");
		}
		this.remains.addAll(this.urls);
		return this.remains;
	}

	@Override
	public void clear() {
		if (this.urls != null) {
			this.urls.clear();
		}
		if (this.urlSet != null) {
			this.urlSet.clear();
		}
		if (this.remains != null) {
			this.remains.clear();
		}
		if (this.nextUrl != null) {
			this.nextUrl = null;
		}
		if (this.locations != null) {
			this.locations.clear();
		}
		if (this.redirectUrls != null) {
			this.redirectUrls.clear();
		}
	}
	
}
