package org.pangdoo.duboo.fetcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.GZIPInputStreamFactory;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.InputStreamFactory;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.pangdoo.duboo.http.HttpRequest;
import org.pangdoo.duboo.http.HttpResponse;
import org.pangdoo.duboo.http.authentication.*;
import org.pangdoo.duboo.util.LogLogger;

public class Fetcher {

    private final static LogLogger logger = LogLogger.getLogger(Fetcher.class);

    public static Fetcher obj() {
        return new Fetcher();
    }

    public static Fetcher custom(Options options) {
        return new Fetcher(options);
    }

    protected Fetcher() {
        this.options = Options.opts();
    }

    protected Fetcher(Options options) {
        this.options = options;
    }

    protected CloseableHttpClient httpClient;
    protected Credential credential;
    protected Options options;

    private final static CookieStore COOKIE_STORE = new BasicCookieStore();

    public Fetcher credential(Credential credential) {
        this.credential = credential;
        return this;
    }

    public Fetcher build() {
        if (null == options) {
            throw new FetcherInitialException("Options is null.");
        }
        Builder builder = RequestConfig.custom()
                .setExpectContinueEnabled(false)
                .setCookieSpec(options.getCookieSpec())
                .setRedirectsEnabled(false)
                .setConnectionRequestTimeout(options.getConnectionRequestTimeout())
                .setSocketTimeout(options.getSocketTimeout())
                .setConnectTimeout(options.getConnectTimeout());
        if (options.getProxy() != null) {
            builder = builder.setProxy(new HttpHost(options.getProxy().getProxyHost(), options.getProxy().getProxyPort()));
        }
        RequestConfig requestConfig = builder.build();
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
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(connRegistryBuilder.build(), new SystemDefaultDnsResolver());
        connectionManager.setMaxTotal(options.getMaxTotalConnections());
        connectionManager.setDefaultMaxPerRoute(options.getMaxConnectionsPerHost());
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(requestConfig);
        clientBuilder.setUserAgent(options.getUserAgent());
        clientBuilder.setConnectionManager(connectionManager);
        if (credential != null) {
            if (credential instanceof  BasicCredential) {
                BasicCredential basicCredential = (BasicCredential) credential;
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(new AuthScope(basicCredential.getHost(), basicCredential.getPort()),
                        new UsernamePasswordCredentials(basicCredential.getUsername(), basicCredential.getPassword()));
                clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        }
        if (options.isGzip()) {
            clientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {

                public void process(
                        final org.apache.http.HttpRequest request,
                        final HttpContext context) {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                }
            });
            Map<String, InputStreamFactory> contentDecoderMap = new HashMap<String, InputStreamFactory>();
            contentDecoderMap.put("gizp", new GZIPInputStreamFactory());
            clientBuilder.setContentDecoderRegistry(contentDecoderMap);
        }
        httpClient = clientBuilder.build();
        return this;
    }

    public HttpResponse doFormLogin() {
        if (this.credential == null) {
            throw new NoCredentialException("Credential is not found.");
        }
        if (!FormCredential.class.isInstance(this.credential)) {
            throw new CredentialTypeErrorException("This credential can not be used for login form.");
        }
        FormCredential formCredential = (FormCredential) this.credential;
        HttpPost httpPost = new HttpPost(formCredential.getLoginUrl());
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair(formCredential.getUsernameKey(), formCredential.getUsername()));
        formParams.add(new BasicNameValuePair(formCredential.getPasswordKey(), formCredential.getPassword()));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8);
        httpPost.setEntity(entity);
        try {
            final HttpClientContext httpContext = HttpClientContext.create();
            httpContext.setCookieStore(COOKIE_STORE);
            CloseableHttpResponse formResponse = httpClient.execute(httpPost, httpContext);
            return parseResponse(formResponse, formCredential.getLoginUrl());
        } catch (IOException e) {
            logger.error("Form Login error : " + formCredential.getLoginUrl(), e);
        }
        return new HttpResponse();
    }

    public HttpResponse fetch(HttpRequest req) {
        try {
            final HttpClientContext httpContext = HttpClientContext.create();
            if (options.getProxy() != null) {
				AuthState authState = new AuthState();
				authState.update(new BasicScheme(ChallengeState.PROXY), new UsernamePasswordCredentials(options.getProxy().getProxyUsername(), options.getProxy().getProxyPassword()));
				httpContext.setAttribute(HttpClientContext.PROXY_AUTH_STATE, authState);
            }
            if (options.getCookies() != null && !options.getCookies().isEmpty()) {
                for (Map.Entry<String, String> cookieEntry : options.getCookies().entrySet()) {
                    BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                    cookie.setDomain(req.getWebURL().getDomain());
                    COOKIE_STORE.addCookie(cookie);
                }
            }
            httpContext.setCookieStore(COOKIE_STORE);
            HttpUriRequest request = req.request();
            CloseableHttpResponse response = httpClient.execute(request, httpContext);
            return parseResponse(response, req.getWebURL().getOrigin());
        } catch (IOException e) {
            logger.error("Error URL : " + req.getWebURL().getOrigin(), e);
        }
        return new HttpResponse();
    }

    private HttpResponse parseResponse(CloseableHttpResponse response, String url) {
        HttpResponse httpResponse = new HttpResponse();
        HttpEntity httpEntity = response.getEntity();
        if (options.isGzip() && httpEntity.isChunked()) {
            httpResponse.setEntity(new GzipDecompressingEntity(httpEntity));
        } else {
            httpResponse.setEntity(httpEntity);
        }
        httpResponse.setHeaders(response.getAllHeaders());
        httpResponse.setLocale(response.getLocale());
        StatusLine statusLine = response.getStatusLine();
        logger.info("Fetcher for [{}] : {}", url, statusLine);
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
        return httpResponse;
    }

    public void shutdown() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            logger.warn(e);
        }
    }

    public Options options() {
        return this.options;
    }

}
