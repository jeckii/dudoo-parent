package org.pangdoo.duboo.request.impl;

import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.pangdoo.duboo.exception.NullValueException;
import org.pangdoo.duboo.request.HttpUrlRequst;
import org.pangdoo.duboo.util.StringUtils;

public class BasicHttpGet extends HttpUrlRequst {

	public BasicHttpGet() {
		super();
	}

	public BasicHttpGet(String url) {
		super(url);
	}

	@Override
	public HttpUriRequest request() throws Exception {
		if (StringUtils.isEmpty(this.url)) {
			throw new NullValueException("URL is null.");
		}
		RequestBuilder requestBuilder = RequestBuilder.get(this.url);
		Map<String, String> header = getHeaders();
		if (header != null && !header.isEmpty()) {
			Iterator<String> headerIterator = header.keySet()
					.iterator();
			while (headerIterator.hasNext()) {
				String name = headerIterator.next();
				requestBuilder.addHeader(name, header.get(name));
			}
		}
		return requestBuilder.build();
	}

}
