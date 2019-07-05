package org.pangdoo.duboo.crawler;

import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.Options;
import org.pangdoo.duboo.processor.Processor;
import org.pangdoo.duboo.http.HttpResponse;
import org.pangdoo.duboo.http.authentication.*;
import org.pangdoo.duboo.http.HttpRequest;
import org.pangdoo.duboo.http.basic.BasicHttpGet;
import org.pangdoo.duboo.http.basic.BasicHttpPost;
import org.pangdoo.duboo.robots.Robots;
import org.pangdoo.duboo.robots.RobotsCache;
import org.pangdoo.duboo.url.URLCollector;
import org.pangdoo.duboo.url.WebURL;
import org.pangdoo.duboo.url.collector.DefaultURLCollector;

import java.util.HashMap;
import java.util.Map;

public class Crawler {

    public static Crawler custom() {
        return new Crawler();
    }

    private Crawler() {
    }

    private Options options;

    public Crawler options(Options options) {
        this.options = options;
        return this;
    }

    private WebURL url;

    public Crawler url(String url) {
        this.url = new WebURL(url);
        return this;
    }

    private URLCollector urlCollector;

    public Crawler collector(URLCollector urlCollector) {
        this.urlCollector = urlCollector;
        return this;
    }

    private Processor processor;

    public Crawler process(Processor processor) {
        this.processor = processor;
        return this;
    }

    private Credential credential;

    public Crawler credential(Credential credential) {
        this.credential = credential;
        return this;
    }

    public void get() {
        if (this.credential != null) {
            if (this.credential instanceof FormCredential) {
                formLogin();
            }
            doCrawl(new AuthHttpGet(this.credential));
        } else {
            doCrawl(new BasicHttpGet());
        }
    }

    private Map<String, String> params;

    public Crawler params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public Crawler param(String param, String value) {
        if (this.params == null) {
            this.params = new HashMap<String, String>();
        }
        this.params.put(param, value);
        return this;
    }

    public void post() {
        if (this.credential != null) {
            if (this.credential instanceof FormCredential) {
                formLogin();
            }
            doCrawl(new AuthHttpPost(this.credential, this.params));
        } else {
            doCrawl(new BasicHttpPost(this.params));
        }
    }

    private void formLogin() {
        if (this.options == null) {
            this.options = Options.opts();
        }
        Fetcher.custom(this.options)
                .credential(this.credential)
                .build()
                .doFormLogin();
    }

    private void doCrawl(HttpRequest httpRequest) {
        if (this.options == null) {
            this.options = Options.opts();
        }
        Fetcher fetcher = Fetcher.custom(this.options)
                .credential(httpRequest.getCredential())
                .build();
        if (this.urlCollector == null) {
            this.urlCollector = new DefaultURLCollector();
        }
        URLCollector urlCollector = this.urlCollector;
        if (this.url != null) {
            urlCollector.add(this.url.getOrigin(), this.url.getDepth());
        }
        while (urlCollector.hasNext()) {
            WebURL url = urlCollector.consume();
            if (!RobotsCache.hasLocation(url.getOrigin())) {
                Robots.custom(this.options).fetch(url.getScheme() + "://" + url.getLocation());
            }
            if (RobotsCache.hasLocation(url.getOrigin())) {
                return;
            }
            httpRequest.setWebURL(url);
            HttpResponse httpResponse = fetcher.fetch(httpRequest);
            if (this.processor != null) {
                this.processor.process(httpResponse.getEntity(), url);
            }
        }
        fetcher.shutdown();
    }

}
