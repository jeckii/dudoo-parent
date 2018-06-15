package org.pangdoo.duboo.request.impl;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.pangdoo.duboo.url.WebUrl;

public class AuthHttpPost extends BasicHttpPost {
	
	public AuthHttpPost(String username, String password,
			Map<String, String> params) {
		this(null, null, username, password, null, params);
	}
	
	public AuthHttpPost(String username, String password,
			Map<String, String> params, WebUrl webUrl) {
		this(null, null, username, password, null, params, webUrl);
	}
	
	public AuthHttpPost(String username, String password,
			HttpEntity entity, Map<String, String> params) {
		this(null, null, username, password, entity, params);
	}
	
	public AuthHttpPost(String username, String password,
			HttpEntity entity, Map<String, String> params, WebUrl webUrl) {
		this(null, null, username, password, entity, params, webUrl);
	}

	public AuthHttpPost(String host, Integer port, String username, String password,
			HttpEntity entity, Map<String, String> params) {
		this(host, port, username, password, entity, params, null);
	}

	public AuthHttpPost(String host, Integer port, String username, String password,
			HttpEntity entity, Map<String, String> params, WebUrl webUrl) {
		super(entity, params, webUrl);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(host, port),
				new UsernamePasswordCredentials(username, password));
		setCredsProvider(credsProvider);
	}

}
