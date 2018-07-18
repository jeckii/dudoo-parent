package org.pangdoo.duboo.fetcher;

import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.ssl.SSLContexts;
import org.pangdoo.duboo.request.HttpUrlRequst;
import org.pangdoo.duboo.util.LogLogger;

public class Fetcher {
	
	private LogLogger logger = LogLogger.getLogger(Fetcher.class);
	
	protected Fetcher() {
	}
	
	protected Fetcher(Configuration config) {
		this.config = config;
	}
	
	protected Configuration config;
	protected PoolingHttpClientConnectionManager connectionManager;
    protected CloseableHttpClient httpClient;
    protected CloseableHttpResponse response;
    protected CredentialsProvider credsProvider;
    
    public Fetcher config(Configuration config) {
    	if (this.config != null) {
    		
    	}
    	this.config = config;
    	return this;
    }
    
    public Fetcher provider(CredentialsProvider credsProvider) {
    	this.credsProvider = credsProvider;
    	return this;
    }
    
    public Fetcher build() {
    	 RequestConfig requestConfig = RequestConfig.custom()
                 .setExpectContinueEnabled(false)
                 .setCookieSpec(config.getCookieSpec())
                 .setRedirectsEnabled(false)
                 .setConnectionRequestTimeout(config.getConnectionRequestTimeout())
                 .setSocketTimeout(config.getSocketTimeout())
                 .setConnectTimeout(config.getConnectTimeout())
                 .build();
    	 RegistryBuilder<ConnectionSocketFactory> connRegistryBuilder = RegistryBuilder.create();
    	 connRegistryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);
    	 try {
			SSLContext sslContext =
			         SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
			             @Override
			             public boolean isTrusted(final X509Certificate[] chain, String authType) {
			                 return true;
			             }
			         }).build();
			 SSLConnectionSocketFactory sslsf =
			         new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
			 connRegistryBuilder.register("https", sslsf);
    	 } catch (Exception e) {
    		 logger.warn(e);
    	 }
    	 connectionManager = new PoolingHttpClientConnectionManager(connRegistryBuilder.build(), new SystemDefaultDnsResolver());
    	 connectionManager.setMaxTotal(config.getMaxTotalConnections());
         connectionManager.setDefaultMaxPerRoute(config.getMaxConnectionsPerHost());
         HttpClientBuilder clientBuilder = HttpClientBuilder.create();
         if (config.getCookieStore() != null) {
             clientBuilder.setDefaultCookieStore(config.getCookieStore());
         }
         clientBuilder.setDefaultRequestConfig(requestConfig);
         clientBuilder.setUserAgent(config.getUserAgent());
         clientBuilder.setConnectionManager(connectionManager);
         if (credsProvider != null) {
        	 clientBuilder.setDefaultCredentialsProvider(credsProvider);
         }
         httpClient = clientBuilder.build();
         return this;
    }
    
    public HttpResponse fetch(HttpUrlRequst urlRequst) {
    	HttpResponse httpResponse = new HttpResponse();
    	try {
    		HttpUriRequest request = urlRequst.request();
    		response = httpClient.execute(request);
			httpResponse.setEntity(response.getEntity());
			httpResponse.setHeaders(response.getAllHeaders());
			httpResponse.setLocale(response.getLocale());
			StatusLine statusLine = response.getStatusLine();
			httpResponse.setStatusLine(statusLine);
			int status = statusLine.getStatusCode();
			if (status == HttpStatus.SC_MOVED_PERMANENTLY ||
					status == HttpStatus.SC_MOVED_TEMPORARILY ||
					status == HttpStatus.SC_MULTIPLE_CHOICES ||
					status == HttpStatus.SC_SEE_OTHER ||
					status == HttpStatus.SC_TEMPORARY_REDIRECT ||
					status == 308) {
				 Header header = response.getFirstHeader(HttpHeaders.LOCATION);
	                if (header != null) {
	                	httpResponse.setRedirectUrl(header.getValue());
	                }
			}
    	} catch(Exception e) {
    		logger.warn("Error URI : " + urlRequst.getUrl().getUrl(), e);
		}
    	return httpResponse;
    }
    
    public void shutdown() {
    	try {
			if (response != null) {
				response.close();
			}
			if (httpClient != null) {
    			httpClient.close();	
    		}
		} catch (IOException e) {
			logger.warn(e);
		}
    }
    
}
