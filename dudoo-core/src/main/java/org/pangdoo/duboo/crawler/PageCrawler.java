package org.pangdoo.duboo.crawler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.HttpResponse;
import org.pangdoo.duboo.handler.PageParser;
import org.pangdoo.duboo.handler.reader.HTMLReader;
import org.pangdoo.duboo.request.AbstractUrlRequst;
import org.pangdoo.duboo.robots.RobotsTxtFecher;
import org.pangdoo.duboo.url.UrlCollector;
import org.pangdoo.duboo.url.WebUrl;
import org.pangdoo.duboo.url.collector.DefaultUrlCollector;

public class PageCrawler {
	
	private UrlCollector collector;
	private PageParser parser;
	private Configuration configuration;
	
	public PageCrawler(Configuration configuration, Collection<String> urls, PageParser parser) {
		this(configuration, urls, 1, parser);
	}
	
	public PageCrawler(Configuration configuration, Collection<String> urls, int depth, PageParser parser) {
		this.configuration = configuration;
		collector = new DefaultUrlCollector(urls, depth);
		collector.locations().forEach(location -> {
			new RobotsTxtFecher(this.configuration, location)
			.disallow().forEach(v -> {
				collector.filter(location, v);
			});
		});
		if (parser == null) {
			throw new IllegalArgumentException("Parser is null.");
		}
		this.parser = parser;
	}
	
	public List<Object> crawl(AbstractUrlRequst urlRequst, int delay) throws Exception {
		long size = collector.size();
		if (size == 0L) {
			return new ArrayList<Object>(0);
		}
		List<Object> dataList = new ArrayList<Object>(new Long(size).intValue());
		Fetcher fetcher;
		HTMLReader reader;
		while (collector.hasNext()) {
			WebUrl webUrl = collector.consume();
			fetcher = new Fetcher(configuration);
			urlRequst.setUrl(webUrl.getUrl().toString());
			HttpResponse response = fetcher.fetch(urlRequst);
			reader = new HTMLReader(response.getEntity().getContent(), "UTF-8", webUrl.getUrl().baseUrl());
			dataList.add(parser.parse(reader.getDocument()));
			fetcher.close();
			Thread.sleep(delay);
		}
		return dataList;
	}
	
}
