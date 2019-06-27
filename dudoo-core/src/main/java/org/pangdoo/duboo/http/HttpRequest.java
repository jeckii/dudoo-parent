package org.pangdoo.duboo.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpUriRequest;
import org.pangdoo.duboo.url.IllegalURLException;
import org.pangdoo.duboo.url.WebURL;

public abstract class HttpRequest {

	protected WebURL webURL;
	
	protected String charset = "UTF-8";
	
	protected Map<String, String> headers;

	protected CredentialsProvider credsProvider;

	public void setWebURL(WebURL webURL) {
		this.webURL = webURL;
	}

	public WebURL getWebURL() {
		return webURL;
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

	public HttpRequest(WebURL webURL) {
		this.webURL = webURL;
	}
	
	public void setProxy(String proxyHost, int proxyPort, String proxyUsername, String proxyPassword) {
		if (this.credsProvider == null) {
			throw new NoCredentialException("Credentials provider is null.");
		}
		this.credsProvider.setCredentials(new AuthScope(proxyHost, proxyPort),
				new UsernamePasswordCredentials(proxyUsername, proxyPassword));
	}
	
	public HttpRequest() {
	}

	public abstract HttpUriRequest request();

}
