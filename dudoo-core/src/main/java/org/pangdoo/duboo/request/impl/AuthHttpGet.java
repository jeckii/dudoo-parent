package org.pangdoo.duboo.request.impl;

import java.util.Iterator;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.pangdoo.duboo.exception.NullValueException;
import org.pangdoo.duboo.request.HttpUrlRequst;
import org.pangdoo.duboo.util.StringUtils;

public class AuthHttpGet extends HttpUrlRequst {
	
	public AuthHttpGet(String username, String password) {
		this(null, null, username, password, null);
	}

	public AuthHttpGet(String host, Integer port, String username, String password) {
		this(host, port, username, password, null);
	}

	public AuthHttpGet(String host, Integer port, String username, String password, String url) {
		super(url);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(host, port), new UsernamePasswordCredentials(username, password));
		setCredsProvider(credsProvider);
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
