package org.pangdoo.duboo.crawler;

import java.util.Collection;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.exception.NullValueException;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.handler.MultiLoader;
import org.pangdoo.duboo.request.AbstractUrlRequst;
import org.pangdoo.duboo.robots.RobotsTxtFecher;
import org.pangdoo.duboo.url.UrlCollector;
import org.pangdoo.duboo.url.UrlResolver;
import org.pangdoo.duboo.url.WebUrl;
import org.pangdoo.duboo.url.collector.DefaultUrlCollector;
import org.pangdoo.duboo.util.StringUtils;

public class MultiCrawler {
	
	private UrlCollector collector;
	private MultiLoader multiLoader;
	private Configuration configuration;
	
	public MultiCrawler(Configuration configuration, Collection<String> urls, MultiLoader multiLoader) {
		this(configuration, urls, 1, multiLoader);
	}
	
	public MultiCrawler(Configuration configuration, Collection<String> urls, int depth, MultiLoader multiLoader) {
		this.configuration = configuration;
		collector = new DefaultUrlCollector(urls, depth);
		collector.locations().forEach(location -> {
			new RobotsTxtFecher(this.configuration, location)
			.disallow().forEach(v -> {
				collector.filter(location, v);
			});
		});
		if (multiLoader == null) {
			throw new IllegalArgumentException("Loader is null.");
		}
		this.multiLoader = multiLoader;
	}
	
	public void crawl(AbstractUrlRequst urlRequst, int delay) throws Exception {
		Fetcher fetcher = null;
		while (collector.hasNext()) {
			WebUrl webUrl = collector.consume();
			String url = webUrl.getUrl().toString();
			urlRequst.setUrl(url);
			fetcher = new Fetcher(configuration);
			HttpEntity entity = fetcher.fetch(urlRequst).getEntity();
			if (entity == null) {
				throw new NullValueException("Http entity is null.");
			}
			multiLoader.load(entity.getContent(), getMultiName(url));
			fetcher.close();
			Thread.sleep(delay);
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
