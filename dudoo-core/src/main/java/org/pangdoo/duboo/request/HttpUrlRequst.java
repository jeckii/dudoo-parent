package org.pangdoo.duboo.request;

import java.util.Map;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpUriRequest;

public abstract class HttpUrlRequst {

	protected String url;

	protected Map<String, String> headers;

	protected CredentialsProvider credsProvider;

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public CredentialsProvider getCredsProvider() {
		return credsProvider;
	}

	public void setCredsProvider(CredentialsProvider credsProvider) {
		this.credsProvider = credsProvider;
	}

	public HttpUrlRequst(String url) {
		this.url = url;
	}

	public HttpUrlRequst() {
	}

	public abstract HttpUriRequest request() throws Exception;

}
