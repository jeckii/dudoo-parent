package org.pangdoo.duboo.request.impl;

import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.pangdoo.duboo.exception.NullValueException;
import org.pangdoo.duboo.request.HttpUrlRequst;
import org.pangdoo.duboo.util.StringUtils;

public class BasicHttpPost extends HttpUrlRequst {

	protected HttpEntity entity;
	protected Map<String, String> params;

	public BasicHttpPost(HttpEntity entity, Map<String, String> params) {
		this(entity, null, params);
	}

	public BasicHttpPost(HttpEntity entity, String url, Map<String, String> params) {
		super(url);
		this.entity = entity;
		this.params = params;
	}

	@Override
	public HttpUriRequest request() throws Exception {
		if (StringUtils.isEmpty(this.url)) {
			throw new NullValueException("URL is null.");
		}
		RequestBuilder requestBuilder = RequestBuilder.post(this.url);
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
		return requestBuilder.build();
	}

}
