package org.pangdoo.duboo.crawler;

import java.util.Set;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.FetcherBuilder;
import org.pangdoo.duboo.handler.MultiLoader;
import org.pangdoo.duboo.request.HttpUrlRequst;
import org.pangdoo.duboo.robots.RobotsCache;
import org.pangdoo.duboo.robots.RobotstxtFecher;
import org.pangdoo.duboo.url.UrlCollector;
import org.pangdoo.duboo.url.UrlResolver;
import org.pangdoo.duboo.url.WebUrl;

public class MultiCrawler {
	
	private MultiLoader multiLoader;
	private Configuration configuration;
	private RobotstxtFecher robotsTxtFecher;
	
	public MultiCrawler(Configuration configuration, MultiLoader multiLoader) {
		this.configuration = configuration;
		this.robotsTxtFecher = new RobotstxtFecher(configuration);
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
			collector.filter(location);
		}
		Fetcher fetcher = FetcherBuilder.custom()
				.config(configuration)
				.provider(urlRequst.getCredsProvider())
				.build();
		while (collector.hasNext()) {
			WebUrl webUrl = collector.consume();
			urlRequst.setUrl(webUrl);
			HttpEntity entity = fetcher.fetch(urlRequst).getEntity();
			if (entity != null) {
				multiLoader.load(entity, UrlResolver.multiName(webUrl.getUrl().toString()));
			}
			Thread.sleep(configuration.getDelay());
		}
		if (fetcher != null) {
			fetcher.shutdown();
		}
	}
	
}
