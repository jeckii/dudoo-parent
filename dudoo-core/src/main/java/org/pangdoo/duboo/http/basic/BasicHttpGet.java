package org.pangdoo.duboo.http.basic;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.pangdoo.duboo.exception.NullException;
import org.pangdoo.duboo.http.HttpRequest;
import org.pangdoo.duboo.url.Url;
import org.pangdoo.duboo.url.WebUrl;

public class BasicHttpGet extends HttpRequest {

	public BasicHttpGet() {
		super();
	}

	public BasicHttpGet(WebUrl webUrl) {
		super(webUrl);
	}

	@Override
	public HttpUriRequest request() {
		if (this.webUrl == null) {
			throw new NullException("URL is null.");
		}
		Url url = this.webUrl.getUrl();
		if (url == null) {
			throw new NullException("Unable to resolve to URL.");
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
