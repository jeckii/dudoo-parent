package org.pangdoo.duboo.fetcher;

import org.apache.http.client.config.CookieSpecs;
import org.pangdoo.duboo.http.Proxy;
import org.pangdoo.duboo.util.LogLogger;

import java.util.HashMap;
import java.util.Map;

public class Options {

    private final LogLogger logger = LogLogger.getLogger(this.getClass());

    /**
     * Initialize the Options of Fetcher.
     * @return
     */
    public static Options opts() {
        return new Options();
    }

    private Options() {
    }

    private Integer maxDepth = 1;

    private String cookieSpec = CookieSpecs.STANDARD;

    private int connectTimeout = 10000;

    private int connectionRequestTimeout = 5000;

    private int socketTimeout = 1000;

    private int maxConnectionsPerHost = 100;

    private int maxTotalConnections = 100;

    private Map<String, String> cookies = new HashMap<String, String>();

    private String userAgent = "dudoo";

    private int delay = 1000;

    private String charset = "UTF-8";

    private boolean gzip = false;

    public Integer getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
    }

    public String getCookieSpec() {
        return cookieSpec;
    }

    public void setCookieSpec(String cookieSpec) {
        this.cookieSpec = cookieSpec;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getMaxConnectionsPerHost() {
        return maxConnectionsPerHost;
    }

    public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
    }

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isGzip() {
        return gzip;
    }

    public void setGzip(boolean gzip) {
        this.gzip = gzip;
    }

    private Proxy proxy;

    public Options setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public Proxy getProxy() {
        return proxy;
    }

}
