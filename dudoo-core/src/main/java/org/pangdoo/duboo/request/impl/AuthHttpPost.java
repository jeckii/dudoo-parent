package org.pangdoo.duboo.request.impl;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.pangdoo.duboo.url.WebUrl;

public class AuthHttpPost extends BasicHttpPost {
	
	public AuthHttpPost(HttpEntity entity, Map<String, String> params,
			String username, String password) {
		this(entity, null, params, null, null, username, password);
	}

	public AuthHttpPost(HttpEntity entity, Map<String, String> params, String host,
			Integer port, String username, String password) {
		this(entity, null, params, host, port, username, password);
	}

	public AuthHttpPost(HttpEntity entity, WebUrl webUrl, Map<String, String> params,
			String host, Integer port, String username, String password) {
		super(entity, webUrl, params);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(host, port),
				new UsernamePasswordCredentials(username, password));
		setCredsProvider(credsProvider);
	}

}
