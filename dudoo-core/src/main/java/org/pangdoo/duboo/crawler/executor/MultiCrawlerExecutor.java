package org.pangdoo.duboo.crawler.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.exception.NullException;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.FetcherBuilder;
import org.pangdoo.duboo.handler.MultiLoader;
import org.pangdoo.duboo.request.HttpUrlRequst;
import org.pangdoo.duboo.url.UrlResolver;
import org.pangdoo.duboo.url.WebUrl;
import org.pangdoo.duboo.util.LogLogger;

public class MultiCrawlerExecutor {
	
	private LogLogger logger = LogLogger.getLogger(MultiCrawlerExecutor.class);
	
	private ScheduledExecutorService executors;
	
	private static final int DEFAULT_POOL_SIZE = 3;
	
	public MultiCrawlerExecutor() {
		this(DEFAULT_POOL_SIZE);
	}
	
	public MultiCrawlerExecutor(int poolSize) {
		executors = Executors.newScheduledThreadPool(DEFAULT_POOL_SIZE);
	}
	
	public void run(Configuration configuration, HttpUrlRequst urlRequst, MultiLoader multiLoader) {
		executors.schedule(new Runnable() {

			@Override
			public void run() {
				WebUrl webUrl = urlRequst.getUrl();
				if (webUrl == null) {
					try {
						throw new NullException("URL is null.");
					} catch (NullException e) {
						logger.warn(e);
					}
				}
				Fetcher fetcher = FetcherBuilder.custom()
						.config(configuration)
						.provider(urlRequst.getCredsProvider())
						.build();
				HttpEntity entity = fetcher.fetch(urlRequst).getEntity();
				if (entity != null) {
					multiLoader.load(entity, UrlResolver.multiName(webUrl.getUrl().toString()));
				}
				if (fetcher != null) {
					fetcher.shutdown();
				}
			}
		}, configuration.getDelay(), TimeUnit.MILLISECONDS);
	}
	
	public void shutdown() {
		if(!executors.isShutdown()) {
			executors.shutdown();
		}
	}

}
