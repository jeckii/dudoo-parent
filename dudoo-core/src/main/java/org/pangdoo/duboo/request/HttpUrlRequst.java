package org.pangdoo.duboo.request;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpUriRequest;
import org.pangdoo.duboo.url.WebUrl;

public abstract class HttpUrlRequst {

	protected WebUrl webUrl;
	
	protected String charset = "UTF-8";
	
	protected Map<String, String> headers;

	protected CredentialsProvider credsProvider;

	public void setUrl(WebUrl webUrl) {
		this.webUrl = webUrl;
	}

	public WebUrl getUrl() {
		return webUrl;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public void addHeader(String name, String value) {
		if (this.headers == null) {
			this.headers = new HashMap<String, String>();
		}
		headers.put(name, value);
	}

	public CredentialsProvider getCredsProvider() {
		return credsProvider;
	}

	public void setCredsProvider(CredentialsProvider credsProvider) {
		this.credsProvider = credsProvider;
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public HttpUrlRequst(WebUrl webUrl) {
		this.webUrl = webUrl;
	}
	
	public HttpUrlRequst() {
	}

	public abstract HttpUriRequest request() throws Exception;

}
