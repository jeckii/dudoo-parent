package org.pangdoo.duboo.fetcher;

import org.pangdoo.duboo.http.Proxy;

public class Options {

    Configuration config;

    Options(Configuration configuration) {
        this.config = configuration;
    }

    private Options() {
    }

    public Options setProxy(Proxy proxy) {
        this.config.setProxyHost(proxy.getProxyHost());
        this.config.setProxyPort(proxy.getProxyPort());
        this.config.setProxyUsername(proxy.getProxyUsername());
        this.config.setProxyPassword(proxy.getProxyPassword());
        return this;
    }

    public Options setUserAgent(String userAgent) {
        this.config.setUserAgent(userAgent);
        return this;
    }

    public Options setCharset(String charset) {
        this.config.setCharset(charset);
        return this;
    }

}
