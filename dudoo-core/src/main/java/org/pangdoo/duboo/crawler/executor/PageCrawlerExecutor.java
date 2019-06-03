package org.pangdoo.duboo.crawler.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.exception.NullException;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.FetcherBuilder;
import org.pangdoo.duboo.http.HttpResponse;
import org.pangdoo.duboo.handler.PageParser;
import org.pangdoo.duboo.http.HttpRequest;
import org.pangdoo.duboo.url.WebUrl;
import org.pangdoo.duboo.util.LogLogger;

public class PageCrawlerExecutor {
	
	private LogLogger logger = LogLogger.getLogger(PageCrawlerExecutor.class);
	
	private ScheduledExecutorService executors;
	
	private static final int DEFAULT_POOL_SIZE = 3;
	
	public PageCrawlerExecutor() {
		this(DEFAULT_POOL_SIZE);
	}
	
	public PageCrawlerExecutor(int poolSize) {
		executors = Executors.newScheduledThreadPool(poolSize);
	}
	
	public Object run(Configuration configuration, HttpRequest request, PageParser parser) {
		Future<Object> future = executors.schedule(new Callable<Object>() {

			@Override
			public Object call() {
				WebUrl webUrl = request.getUrl();
				if (webUrl == null) {
					throw new NullException("URL is null.");
				}
				Fetcher fetcher = FetcherBuilder.custom()
						.provider(request.getCredsProvider())
						.build();
				HttpResponse response = fetcher.fetch(request);
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
		}, configuration.getDelay(), TimeUnit.MILLISECONDS);
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
