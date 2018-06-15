package org.pangdoo.dudoo.fetcher;

import org.junit.Test;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.FetcherBuilder;
import org.pangdoo.duboo.fetcher.HttpResponse;
import org.pangdoo.duboo.request.impl.BasicHttpGet;
import org.pangdoo.duboo.url.WebUrl;

public class FetcherIO {
	
	@Test
	public void fetche() throws Exception {
		Fetcher fetcher = FetcherBuilder.build(new Configuration());
		HttpResponse response = fetcher.fetch(new BasicHttpGet(new WebUrl("https://blog.csdn.net/")));
		System.out.println(response.getStatusLine());
		fetcher.shutdown();
		response = fetcher.fetch(new BasicHttpGet(new WebUrl("https://blog.csdn.net/")));
		System.out.println(response.getStatusLine());
		fetcher.shutdown();
	}

}
