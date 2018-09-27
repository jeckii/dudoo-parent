package org.pangdoo.duboo.request.impl;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.pangdoo.duboo.url.WebUrl;

public class AuthHttpPost extends BasicHttpPost {
	
	public AuthHttpPost(String host, Integer port, Credentials credentials,
			HttpEntity entity, Map<String, String> params) {
		this(host, port, credentials, entity, params, null);
	}

	public AuthHttpPost(String host, Integer port, Credentials credentials,
			HttpEntity entity, Map<String, String> params, WebUrl webUrl) {
		super(entity, params, webUrl);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(host, port), credentials);
		setCredsProvider(credsProvider);
	}

}
