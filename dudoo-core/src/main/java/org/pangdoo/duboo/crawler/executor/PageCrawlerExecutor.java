package org.pangdoo.duboo.crawler.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.FetcherBuilder;
import org.pangdoo.duboo.fetcher.HttpResponse;
import org.pangdoo.duboo.handler.PageParser;
import org.pangdoo.duboo.request.HttpUrlRequst;
import org.pangdoo.duboo.url.WebUrl;
import org.pangdoo.duboo.util.LogLogger;

public class PageCrawlerExecutor {
	
	private LogLogger logger = LogLogger.getLogger(PageCrawlerExecutor.class);
	
	private ExecutorService executors;
	
	private static final int DEFAULT_POOL_SIZE = 3;
	
	public PageCrawlerExecutor() {
		this(DEFAULT_POOL_SIZE);
	}
	
	public PageCrawlerExecutor(int poolSize) {
		executors = Executors.newFixedThreadPool(poolSize);
	}
	
	public Object run(Configuration configuration, WebUrl webUrl, HttpUrlRequst urlRequst, PageParser parser) {
		Future<Object> future = executors.submit(new Callable<Object>() {

			@Override
			public Object call() {
				urlRequst.setUrl(webUrl);
				Fetcher fetcher = FetcherBuilder.custom()
						.config(configuration)
						.provider(urlRequst.getCredsProvider())
						.build();
				HttpResponse response = fetcher.fetch(urlRequst);
				HttpEntity entity = response.getEntity();
				if (entity == null) {
					logger.warn("Abnormal crawl instruction ： " + webUrl.getOriginalUrl());
				}
				try {
					return parser.parse(entity, webUrl.getUrl().baseUrl());
				} catch (Exception e) {
					logger.warn(e);
				} finally {
					if (fetcher != null) {
						fetcher.shutdown();
					}
				}
				return null;
			}
		});
		try {
			return future.get();
		} catch (Exception e) {
			logger.warn(e);
		}
		return null;
	}
	
	public void shutdown() {
		if(!executors.isShutdown()) {
			executors.shutdown();
		}
	}

}
