package org.pangdoo.duboo.crawler;

import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.FetcherBuilder;
import org.pangdoo.duboo.handler.MultiLoader;
import org.pangdoo.duboo.request.HttpUrlRequst;
import org.pangdoo.duboo.robots.RobotsCache;
import org.pangdoo.duboo.robots.RobotsTxtFecher;
import org.pangdoo.duboo.url.UrlCollector;
import org.pangdoo.duboo.url.UrlResolver;
import org.pangdoo.duboo.url.WebUrl;
import org.pangdoo.duboo.util.StringUtils;

public class MultiCrawler {
	
	private MultiLoader multiLoader;
	private Configuration configuration;
	private RobotsTxtFecher robotsTxtFecher;
	
	public MultiCrawler(Configuration configuration, MultiLoader multiLoader) {
		this.configuration = configuration;
		this.robotsTxtFecher = new RobotsTxtFecher(configuration);
		if (multiLoader == null) {
			throw new IllegalArgumentException("Loader is null.");
		}
		this.multiLoader = multiLoader;
	}
	
	public void crawl(HttpUrlRequst urlRequst, UrlCollector collector) throws Exception {
		Set<String> locations = collector.locations();
		for (String location : locations) {
			if (!RobotsCache.hasLocation(location)) {
				robotsTxtFecher.fetch(location);
			}
			List<String> disallows = RobotsCache.get(location).getDisallow();
			for (String disallow : disallows) {
				collector.filter(location, disallow);
			}
		}
		Fetcher fetcher = FetcherBuilder.custom()
				.config(configuration)
				.provider(urlRequst.getCredsProvider())
				.build();
		while (collector.hasNext()) {
			WebUrl webUrl = collector.consume();
			String url = webUrl.getUrl().toString();
			urlRequst.setUrl(url);
			HttpEntity entity = fetcher.fetch(urlRequst).getEntity();
			if (entity != null) {
				multiLoader.load(entity, getMultiName(url));
			}
			Thread.sleep(configuration.getDelay());
		}
		if (fetcher != null) {
			fetcher.shutdown();
		}
	}
	
	private String getMultiName(String url) {
		String path = UrlResolver.parser(url).getPath();
		if (!StringUtils.isBlank(path)) {
			int sepIndex = path.lastIndexOf("/");
			if (sepIndex == -1) {
				return path;
			} else {
				return path.substring(sepIndex + 1);
			}
		}
		return null;
	}

}
