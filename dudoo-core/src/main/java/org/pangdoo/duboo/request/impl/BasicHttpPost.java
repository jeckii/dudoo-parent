package org.pangdoo.duboo.request.impl;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.pangdoo.duboo.exception.NullValueException;
import org.pangdoo.duboo.request.HttpUrlRequst;
import org.pangdoo.duboo.url.Url;
import org.pangdoo.duboo.url.WebUrl;

public class BasicHttpPost extends HttpUrlRequst {

	protected HttpEntity entity;
	protected Map<String, String> params;

	public BasicHttpPost(HttpEntity entity, Map<String, String> params) {
		this(entity, null, params);
	}

	public BasicHttpPost(HttpEntity entity, WebUrl webUrl, Map<String, String> params) {
		super(webUrl);
		this.entity = entity;
		this.params = params;
	}

	@Override
	public HttpUriRequest request() throws Exception {
		if (this.webUrl == null) {
			throw new NullValueException("URL is null.");
		}
		Url url = this.webUrl.getUrl();
		if (url == null) {
			throw new NullValueException("URL is null.");
		}
		RequestBuilder requestBuilder = RequestBuilder.post(url.toString());
		Map<String, String> header = getHeaders();
		if (header != null && !header.isEmpty()) {
			Iterator<String> headerIterator = header.keySet()
					.iterator();
			while (headerIterator.hasNext()) {
				String name = headerIterator.next();
				requestBuilder.addHeader(name, header.get(name));
			}
		}
		if (entity != null) {
			requestBuilder.setEntity(entity);
		}
		if (params != null && !params.isEmpty()) {
			Iterator<String> paramsIterator = params.keySet()
					.iterator();
			while (paramsIterator.hasNext()) {
				String name = paramsIterator.next();
				requestBuilder.addParameter(name, params.get(name));
			}
		}
		requestBuilder.setCharset(Charset.forName(getCharset()));
		return requestBuilder.build();
	}

}
