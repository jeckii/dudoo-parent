package org.pangdoo.duboo.crawler.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
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
		executors.submit(new Runnable() {
			
			@Override
			public void run() {
				String url = webUrl.getUrl().toString();
				urlRequst.setUrl(url);
				Fetcher fetcher = new Fetcher(configuration);
				HttpEntity entity = fetcher.fetch(urlRequst).getEntity();
				if (entity != null) {
					multiLoader.load(entity, getMultiName(url));
				}
				if (fetcher != null) {
					fetcher.shutdown();
				}
			}
		});
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
