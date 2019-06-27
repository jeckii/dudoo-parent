package org.pangdoo.duboo.url.collector;

import java.util.*;

import org.pangdoo.duboo.url.IllegalURLException;
import org.pangdoo.duboo.url.URLCollector;
import org.pangdoo.duboo.url.URLResolver;
import org.pangdoo.duboo.url.WebURL;
import org.pangdoo.duboo.util.LogLogger;
import org.pangdoo.duboo.util.StringUtils;
import org.pangdoo.duboo.robots.RobotsCache;

public final class DefaultURLCollector implements URLCollector {
	
	private LogLogger logger = LogLogger.getLogger(DefaultURLCollector.class);
	
	public DefaultURLCollector() {
	}
	
	public DefaultURLCollector(Collection<String> urls, int depth) {
		add(urls, depth);
	}
	
	public DefaultURLCollector(Collection<String> urls) {
		this(urls, 1);
	}
	private Set<String> urlCollection = Collections.synchronizedSet(new HashSet<String>());
	private Set<WebURL> webUrlCollection = Collections.synchronizedSet(new HashSet<WebURL>());
	
	private WebURL nextUrl;
	
	private Set<WebURL> remains = Collections.synchronizedSet(new HashSet<WebURL>());
	
	private Set<String> locations = Collections.synchronizedSet(new HashSet<String>());
	
	private Map<String, WebURL> redirectMap = new LinkedHashMap<String, WebURL>();

	private Set<WebURL> addUrls(Collection<String> urls, int depth) {
		try {
			Set<WebURL> urlTempSet = new HashSet<WebURL>();
			WebURL webUrl;
			for(String url : urls) {
				webUrl = new WebURL(url, depth);
				String location = webUrl.getUrl().getLocation();
				if (!StringUtils.isBlank(location)) {
					this.locations.add(webUrl.getUrl().getScheme() + "://" + location);
				}
				this.webUrlCollection.add(webUrl);
				urlTempSet.add(webUrl);
			}
			return urlTempSet;
		} catch (Exception e) {
			logger.warn(e);
		}
		return null;
	}
	
	private WebURL addUrl(String url, int depth) {
		WebURL webUrl = new WebURL(url, depth);
		String location = webUrl.getUrl().getLocation();
		if (!StringUtils.isBlank(location)) {
			this.locations.add(webUrl.getUrl().getScheme() + "://" + location);
		}
		this.webUrlCollection.add(webUrl);
		return webUrl;
	}
	
	public WebURL setNextUrl(WebURL nextUrl) {
		removeRemains(nextUrl);
		return this.nextUrl = nextUrl;
	}

	public boolean removeRemains(WebURL url) {
		return this.remains.remove(url);
	}

	@Override
	public Set<WebURL> add(Collection<String> urls, int depth) {
		Set<WebURL> temp = addUrls(urlFilter(urls), depth);
		for (WebURL url : temp) {
			this.remains.add(url);
		}
		return this.remains;
	}
	
	private Set<String> urlFilter(Collection<String> urls) {
		Set<String> rulRtn = new HashSet<String>();
		urls.forEach(url -> {
			if (this.urlCollection.add(url)) {
				rulRtn.add(url);
			}
		});
		return rulRtn;
	}

	@Override
	public WebURL consume() {
		if (this.remains == null) {
			return null;
		}
		if (this.remains.size() == 0) {
			return null;
		}
		return setNextUrl(this.remains.iterator().next());
	}

	@Override
	public WebURL redirect(final String url) {
		WebURL next = this.nextUrl;
		if (next == null) {
			throw new IllegalURLException("Next URL is null");
		}
		WebURL nextUrl = addUrl(url, next.getDepth());
		if (nextUrl != null) {
			this.redirectMap.put(next.getOrigin(), nextUrl);
			this.webUrlCollection.remove(next);
			return setNextUrl(nextUrl);
		}
		return null;
	}
	
	@Override
	public long size() {
		return this.webUrlCollection.size();
	}
	
	@Override
	public boolean hasNext() {
		if (this.remains == null) {
			throw new IllegalURLException("Remains is null.");
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
	public boolean remove(WebURL url) {
		this.webUrlCollection.remove(url);
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
			for (WebURL url : this.webUrlCollection) {
				if (location.equals(url.getUrl().getLocation()) 
						&& StringUtils.pattern(url.getUrl().getPath(), URLResolver.pathPattern(disallow))) {
					if(this.webUrlCollection.remove(url)) {
						count ++;
					}
				}
			}
		}
		return count;
	}
	
	@Override
	public Set<WebURL> rebuild() {
		this.remains.addAll(this.webUrlCollection);
		return this.remains;
	}

	@Override
	public void clear() {
		if (this.webUrlCollection != null) {
			this.webUrlCollection.clear();
		}
		if (this.urlCollection != null) {
			this.urlCollection.clear();
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
		this.redirectMap.clear();
	}
	
}
