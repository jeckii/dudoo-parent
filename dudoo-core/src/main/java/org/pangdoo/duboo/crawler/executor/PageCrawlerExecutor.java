package org.pangdoo.duboo.crawler.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.HttpResponse;
import org.pangdoo.duboo.handler.PageParser;
import org.pangdoo.duboo.handler.reader.HTMLReader;
import org.pangdoo.duboo.request.AbstractUrlRequst;
import org.pangdoo.duboo.url.WebUrl;
import org.pangdoo.duboo.util.LogLogger;

public class PageCrawlerExecutor {
	
	private LogLogger logger = LogLogger.getLogger(PageCrawlerExecutor.class);
	
	private ExecutorService executors;
	
	private static final int POOL_SIZE = 3;
	
	public PageCrawlerExecutor() {
		this(POOL_SIZE);
	}
	
	public PageCrawlerExecutor(int poolSize) {
		executors = Executors.newFixedThreadPool(poolSize);
	}
	
	public Object run(Configuration configuration, WebUrl webUrl, AbstractUrlRequst urlRequst, PageParser parser) {
		urlRequst.setUrl(webUrl.getUrl().toString());
		Fetcher fetcher = new Fetcher(configuration);
		Future<HttpResponse> future = executors.submit(new Callable<HttpResponse>() {
            public HttpResponse call() throws Exception {
                return fetcher.fetch(urlRequst);
            }
        });
		try {
			HttpResponse response = future.get();
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				logger.warn("Abnormal crawl instruction ： " + webUrl.getOriginalUrl());
				return null;
			}
			return parser.parse(new HTMLReader(entity.getContent(), "UTF-8", webUrl.getUrl().baseUrl())
					.getDocument());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (fetcher != null) {
			fetcher.shutdown();
		}
		return null;
	}
	
	public void shutdown() {
		if(!executors.isShutdown()) {
			executors.shutdown();
		}
	}

}
