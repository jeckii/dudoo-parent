package org.pangdoo.duboo.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.FetcherBuilder;
import org.pangdoo.duboo.handler.PageParser;
import org.pangdoo.duboo.request.HttpUrlRequst;
import org.pangdoo.duboo.robots.Robot;
import org.pangdoo.duboo.robots.RobotsCache;
import org.pangdoo.duboo.robots.RobotsTxtFecher;
import org.pangdoo.duboo.url.UrlCollector;
import org.pangdoo.duboo.url.WebUrl;

public class PageCrawler {

	private PageParser parser;
	private Configuration configuration;
	private RobotsTxtFecher robotsTxtFecher;

	public PageCrawler(Configuration configuration, PageParser parser) {
		this.configuration = configuration;
		this.robotsTxtFecher = new RobotsTxtFecher(configuration);
		if (parser == null) {
			throw new IllegalArgumentException("Parser is null.");
		}
		this.parser = parser;
	}

	public List<Object> crawl(HttpUrlRequst urlRequst, UrlCollector collector) throws Exception {
		Set<String> locations = collector.locations();
		for (String location : locations) {
			if (!RobotsCache.hasLocation(location)) {
				robotsTxtFecher.fetch(location);
			}
			Robot robot = RobotsCache.get(location);
			if (robot != null) {
				List<String> disallows = robot.getDisallow();
				for (String disallow : disallows) {
					collector.filter(location, disallow);
				}
			}
		}
		long size = collector.size();
		if (size == 0L) {
			return new ArrayList<Object>(0);
		}
		List<Object> dataList = new ArrayList<Object>(new Long(size).intValue());
		Fetcher fetcher = FetcherBuilder.custom()
				.config(configuration)
				.provider(urlRequst.getCredsProvider())
				.build();
		while (collector.hasNext()) {
			WebUrl webUrl = collector.consume();
			urlRequst.setUrl(webUrl.getUrl().toString());
			HttpEntity entity = fetcher.fetch(urlRequst).getEntity();
			if (entity != null) {
				dataList.add(parser.parse(entity, webUrl.getUrl().baseUrl()));
			}
			Thread.sleep(configuration.getDelay());
		}
		if (fetcher != null) {
			fetcher.shutdown();
		}
		return dataList;
	}

}
