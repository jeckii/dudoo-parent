package org.pangdoo.duboo.http.basic;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.pangdoo.duboo.http.HttpRequest;
import org.pangdoo.duboo.url.IllegalURLException;
import org.pangdoo.duboo.url.NoURLException;
import org.pangdoo.duboo.url.URL;
import org.pangdoo.duboo.url.WebURL;

public class BasicHttpGet extends HttpRequest {

	public BasicHttpGet() {
		super();
	}

	public BasicHttpGet(WebURL webUrl) {
		super(webUrl);
	}

	@Override
	public HttpUriRequest request() {
		if (this.webURL == null) {
			throw new NoURLException("URL is null.");
		}
		URL url = this.webURL.getUrl();
		if (url == null) {
			throw new IllegalURLException("Unable to resolve to URL.");
		}
		RequestBuilder builder = RequestBuilder.get(url.toString());
		Map<String, String> header = getHeaders();
		if (header != null && !header.isEmpty()) {
			Iterator<String> headerIterator = header.keySet()
					.iterator();
			while (headerIterator.hasNext()) {
				String name = headerIterator.next();
				builder.addHeader(name, header.get(name));
			}
		}
		builder.setCharset(Charset.forName(getCharset()));
		return builder.build();
	}

}
