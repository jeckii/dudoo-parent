package org.pangdoo.duboo.crawler.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.exception.NullValueException;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.HttpResponse;
import org.pangdoo.duboo.handler.MultiLoader;
import org.pangdoo.duboo.request.AbstractUrlRequst;
import org.pangdoo.duboo.url.UrlResolver;
import org.pangdoo.duboo.url.WebUrl;
import org.pangdoo.duboo.util.StringUtils;

public class MultiCrawlerExecutor {
	
	private ExecutorService executors;
	
	private static final int POOL_SIZE = 3;
	
	public MultiCrawlerExecutor() {
		this(POOL_SIZE);
	}
	
	public MultiCrawlerExecutor(int poolSize) {
		executors = Executors.newFixedThreadPool(poolSize);
	}
	
	public void run(Configuration configuration, WebUrl webUrl, AbstractUrlRequst urlRequst, MultiLoader multiLoader) {
		String url = webUrl.getUrl().toString();
		urlRequst.setUrl(url);
		Fetcher fetcher = new Fetcher(configuration);
		Future<HttpResponse> future = executors.submit(new Callable<HttpResponse>() {
            public HttpResponse call() throws Exception {
                return fetcher.fetch(urlRequst);
            }
        });
		try {
			HttpEntity entity = future.get().getEntity();
			if (entity == null) {
				throw new NullValueException("Http entity is null.");
			}
			multiLoader.load(entity.getContent(), getMultiName(url));
			fetcher.close();
		} catch (Exception e) {
			e.printStackTrace();
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
	
	public void shutdown() {
		if(!executors.isShutdown()) {
			executors.shutdown();
		}
	}

}
