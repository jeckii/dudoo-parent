package org.pangdoo.duboo.crawler;

import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.Options;
import org.pangdoo.duboo.handler.Handler;
import org.pangdoo.duboo.http.HttpResponse;
import org.pangdoo.duboo.http.authentication.*;
import org.pangdoo.duboo.http.HttpRequest;
import org.pangdoo.duboo.http.basic.BasicHttpGet;
import org.pangdoo.duboo.http.basic.BasicHttpPost;
import org.pangdoo.duboo.robots.Robots;
import org.pangdoo.duboo.robots.RobotsCache;
import org.pangdoo.duboo.url.URL;
import org.pangdoo.duboo.url.WebURL;

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

    private Handler handler;

    public Crawler handler(Handler handler) {
        this.handler = handler;
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
        if (!RobotsCache.hasLocation(this.url.getOrigin())) {
            URL url = this.url.getUrl();
            Robots.custom(this.options).fetch(url.getScheme() + "://" + this.url.getLocation());
        }
        if (RobotsCache.hasLocation(this.url.getOrigin())) {
            return;
        }
        httpRequest.setWebURL(this.url);
        HttpResponse httpResponse = fetcher.fetch(httpRequest);
        if (this.handler != null) {
            this.handler.handle(httpResponse.getEntity(), this.url);
        }
        fetcher.shutdown();
    }
}
