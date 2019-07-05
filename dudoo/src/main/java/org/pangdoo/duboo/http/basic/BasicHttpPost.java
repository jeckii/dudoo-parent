package org.pangdoo.duboo.http.basic;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.pangdoo.duboo.http.HttpRequest;
import org.pangdoo.duboo.url.IllegalURLException;
import org.pangdoo.duboo.url.URLNullErrorException;
import org.pangdoo.duboo.url.URL;
import org.pangdoo.duboo.url.WebURL;

public class BasicHttpPost extends HttpRequest {

	protected Map<String, String> params;
	
	public BasicHttpPost(Map<String, String> params) {
		this(params, null);
	}
	
	public BasicHttpPost(Map<String, String> params, WebURL webUrl) {
	}

	@Override
	public HttpUriRequest request() {
		if (this.webURL == null) {
			throw new URLNullErrorException("URL is null.");
		}
		RequestBuilder builder = RequestBuilder.post(this.webURL.getUrl());
		Map<String, String> header = getHeaders();
		if (header != null && !header.isEmpty()) {
			Iterator<String> headerIterator = header.keySet()
					.iterator();
			while (headerIterator.hasNext()) {
				String name = headerIterator.next();
				builder.addHeader(name, header.get(name));
			}
		}
		if (params != null && !params.isEmpty()) {
			Iterator<String> paramsIterator = params.keySet()
					.iterator();
			while (paramsIterator.hasNext()) {
				String name = paramsIterator.next();
				builder.addParameter(name, params.get(name));
			}
		}
		builder.setCharset(Charset.forName(getCharset()));
		return builder.build();
	}

}
