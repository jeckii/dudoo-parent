package org.pangdoo.duboo.request.impl;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.pangdoo.duboo.exception.NullException;
import org.pangdoo.duboo.request.HttpUrlRequst;
import org.pangdoo.duboo.url.Url;
import org.pangdoo.duboo.url.WebUrl;

public class AuthHttpGet extends HttpUrlRequst {
	
	public AuthHttpGet(String username, String password) {
		this(null, null, username, password, null);
	}
	
	public AuthHttpGet(String username, String password, WebUrl webUrl) {
		this(null, null, username, password, webUrl);
	}

	public AuthHttpGet(String host, Integer port, String username, String password) {
		this(host, port, username, password, null);
	}

	public AuthHttpGet(String host, Integer port, String username, String password, WebUrl webUrl) {
		super(webUrl);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(host, port), new UsernamePasswordCredentials(username, password));
		setCredsProvider(credsProvider);
	}

	@Override
	public HttpUriRequest request() throws Exception {
		if (this.webUrl == null) {
			throw new NullException("URL is null.");
		}
		Url url = this.webUrl.getUrl();
		if (url == null) {
			throw new NullException("URL is null.");
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
