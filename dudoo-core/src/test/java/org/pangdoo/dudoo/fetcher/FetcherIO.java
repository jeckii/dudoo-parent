package org.pangdoo.dudoo.fetcher;

import org.junit.Test;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.HttpResponse;
import org.pangdoo.duboo.request.impl.BasicHttpGet;

public class FetcherIO {
	
	@Test
	public void fetche() {
		Fetcher fetcher = new Fetcher(new Configuration());
		HttpResponse response = fetcher.fetch(new BasicHttpGet("https://blog.csdn.net/"));
		System.out.println(response.getStatusLine());
		fetcher.shutdown();
		response = fetcher.fetch(new BasicHttpGet("https://blog.csdn.net/"));
		System.out.println(response.getStatusLine());
		fetcher.shutdown();
	}

}
