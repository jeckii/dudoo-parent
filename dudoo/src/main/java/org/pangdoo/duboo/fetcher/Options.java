package org.pangdoo.duboo.fetcher;

import org.apache.http.client.config.CookieSpecs;
import org.pangdoo.duboo.http.Proxy;
import org.pangdoo.duboo.util.LogLogger;

import java.util.HashMap;
import java.util.Map;

public class Options {

    private final static LogLogger logger = LogLogger.getLogger(Options.class);

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

    public Options setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    public String getCookieSpec() {
        return cookieSpec;
    }

    public Options setCookieSpec(String cookieSpec) {
        this.cookieSpec = cookieSpec;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public Options setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public Options setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public Options setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public int getMaxConnectionsPerHost() {
        return maxConnectionsPerHost;
    }

    public Options setMaxConnectionsPerHost(int maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
        return this;
    }

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public Options setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
        return this;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Options setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Options setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public int getDelay() {
        return delay;
    }

    public Options setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Options setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public boolean isGzip() {
        return gzip;
    }

    public Options setGzip(boolean gzip) {
        this.gzip = gzip;
        return this;
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
