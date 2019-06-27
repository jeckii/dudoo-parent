package org.pangdoo.duboo.fetcher;

import org.pangdoo.duboo.http.Proxy;
import org.pangdoo.duboo.util.LogLogger;

public class Options {

    private LogLogger logger = LogLogger.getLogger(this.getClass());

    private Configuration config;

    /**
     * Initialize the Options of Fetcher.
     * @return
     */
    public static Options opts() {
        return new Options();
    }

    public static Options custom(Configuration configuration) {
        return new Options(configuration);
    }

    protected Options(Configuration configuration) {
        this.config = configuration;
    }

    protected Options() {
        this.config = new Configuration();
    }

    public Options setProxy(Proxy proxy) {
        try {
            if (null == proxy)
                throw new IllegalAccessException("Proxy is null.");
            this.config.setProxyHost(proxy.getProxyHost());
            this.config.setProxyPort(proxy.getProxyPort());
            this.config.setProxyUsername(proxy.getProxyUsername());
            this.config.setProxyPassword(proxy.getProxyPassword());
            return this;
        } catch (IllegalAccessException e) {
            logger.error(e);
        }
        return null;
    }

    public Options setUserAgent(String userAgent) {
        this.config.setUserAgent(userAgent);
        return this;
    }

    public Options setCharset(String charset) {
        this.config.setCharset(charset);
        return this;
    }

    public Configuration config() {
        return this.config;
    }

}
