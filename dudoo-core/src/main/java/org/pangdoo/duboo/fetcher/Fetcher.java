package org.pangdoo.duboo.fetcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.GZIPInputStreamFactory;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.InputStreamFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.ssl.SSLContexts;
import org.pangdoo.duboo.http.HttpRequest;
import org.pangdoo.duboo.http.HttpResponse;
import org.pangdoo.duboo.util.LogLogger;
import org.pangdoo.duboo.util.StringUtils;

public class Fetcher {

    private LogLogger logger = LogLogger.getLogger(Fetcher.class);

    public static Fetcher obj() {
        return new Fetcher();
    }

    public static Fetcher custom(Options options) {
        return new Fetcher(options);
    }

    protected Fetcher() {
        this.options = new Options();
    }

    protected Fetcher(Options options) {
        this.options = options;
    }

    protected PoolingHttpClientConnectionManager connectionManager;
    protected CloseableHttpClient httpClient;
    protected CloseableHttpResponse response;
    protected CredentialsProvider credsProvider;
    protected Options options;

    public Fetcher provider(CredentialsProvider credsProvider) {
        this.credsProvider = credsProvider;
        return this;
    }

    public Fetcher build() {
        if (null == options) {
            throw new FetcherInitialException("Options is null.");
        }
        Builder builder = RequestConfig.custom()
                .setExpectContinueEnabled(false)
                .setCookieSpec(config().getCookieSpec())
                .setRedirectsEnabled(false)
                .setConnectionRequestTimeout(config().getConnectionRequestTimeout())
                .setSocketTimeout(config().getSocketTimeout())
                .setConnectTimeout(config().getConnectTimeout());
        if (config().getProxyHost() != null) {
            builder = builder.setProxy(new HttpHost(config().getProxyHost(), config().getProxyPort()));
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
        connectionManager = new PoolingHttpClientConnectionManager(connRegistryBuilder.build(), new SystemDefaultDnsResolver());
        connectionManager.setMaxTotal(config().getMaxTotalConnections());
        connectionManager.setDefaultMaxPerRoute(config().getMaxConnectionsPerHost());
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(requestConfig);
        clientBuilder.setUserAgent(config().getUserAgent());
        clientBuilder.setConnectionManager(connectionManager);
        if (credsProvider != null) {
            clientBuilder.setDefaultCredentialsProvider(credsProvider);
        }
        if (config().isGzip()) {
            clientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {

                public void process(
                        final org.apache.http.HttpRequest request,
                        final HttpContext context) throws HttpException, IOException {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
//                        request.addHeader("content-type", "text/html; charset=UTF-8");
                    }
                }
            });
            Map<String, InputStreamFactory> contentDecoderMap = new HashMap<String, InputStreamFactory>();
            contentDecoderMap.put("gizp", new GZIPInputStreamFactory());
            clientBuilder.setContentDecoderRegistry(contentDecoderMap);
            /*clientBuilder.setHttpProcessor(new HttpProcessor() {

                @Override
                public void process(org.apache.http.HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
                    Header[] contentEncodingHeaders = httpResponse.getHeaders("Content-Encoding");
                    if( contentEncodingHeaders[0].getValue().contains("gzip") ) {
                        try(
                                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                                InputStream responseStream = new GZIPInputStream(httpResponse.getEntity().getContent());
                        ) {
                            byte[] buffer = new byte[4096];
                            int length;
                            while((length=responseStream.read(buffer))>0){
                                outStream.write(buffer,0,length);
                            }
                            System.out.println(new String(outStream.toByteArray()));
                        }
                        catch(Exception exception) {
                            throw exception;
                        }
                    }
                }

                @Override
                public void process(org.apache.http.HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {

                }
            });*/
        }
        httpClient = clientBuilder.build();
        return this;
    }

    public HttpResponse fetch(HttpRequest req) {
        HttpResponse httpResponse = new HttpResponse();
        try {
            final HttpClientContext httpContext = new HttpClientContext();
            if (!StringUtils.isBlank(config().getProxyHost())) {
                req.setProxy(config().getProxyHost(), config().getProxyPort(), config().getProxyUsername(),
                        config().getProxyPassword());
//				AuthState authState = new AuthState();
//				authState.update(new BasicScheme(ChallengeState.PROXY), new UsernamePasswordCredentials(config().getProxyUsername(), config().getProxyPassword()));
//				httpContext.setAttribute(HttpClientContext.PROXY_AUTH_STATE, authState);
            }
            if (config().getCookies() != null && !config().getCookies().isEmpty()) {
                CookieStore cookieStore = new BasicCookieStore();
                for (Map.Entry<String, String> cookieEntry : config().getCookies().entrySet()) {
                    BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                    cookie.setDomain(req.getWebURL().getDomain());
                    cookieStore.addCookie(cookie);
                }
                httpContext.setCookieStore(cookieStore);
            }
            HttpUriRequest request = req.request();
            response = httpClient.execute(request, httpContext);
            HttpEntity httpEntity = response.getEntity();
            if (config().isGzip() && httpEntity.isChunked()) {
                httpResponse.setEntity(new GzipDecompressingEntity(httpEntity));
            } else {
                httpResponse.setEntity(httpEntity);
            }
            httpResponse.setHeaders(response.getAllHeaders());
            httpResponse.setLocale(response.getLocale());
            StatusLine statusLine = response.getStatusLine();
            logger.info("Fetcher for [{}] : {}", req.getWebURL().getOrigin(), statusLine);
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
        } catch (IOException e) {
            logger.error("Error URI : " + req.getWebURL().getOrigin(), e);
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

    public Options options() {
        return this.options;
    }

    protected Configuration config() {
        return this.options.config();
    }

}
