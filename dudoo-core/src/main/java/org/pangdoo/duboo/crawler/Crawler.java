package org.pangdoo.duboo.crawler;

import org.apache.http.HttpEntity;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.pangdoo.duboo.fetcher.Fetcher;
import org.pangdoo.duboo.fetcher.Options;
import org.pangdoo.duboo.handler.Handler;
import org.pangdoo.duboo.http.Credential;
import org.pangdoo.duboo.http.HttpRequest;
import org.pangdoo.duboo.http.auth.AuthHttpGet;
import org.pangdoo.duboo.http.basic.BasicHttpGet;
import org.pangdoo.duboo.http.basic.BasicHttpPost;
import org.pangdoo.duboo.robots.Robots;
import org.pangdoo.duboo.robots.RobotsCache;
import org.pangdoo.duboo.url.URL;
import org.pangdoo.duboo.url.WebURL;

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

    private Crawler credential(Credential credential) {
        this.credential = credential;
        return this;
    }

    public void get() {
        if (this.credential != null) {
            crawl(new AuthHttpGet(this.credential.getHost(), this.credential.getPort(),
                    new UsernamePasswordCredentials(this.credential.getUsername(), this.credential.getPassword())));
        } else {
            crawl(new BasicHttpGet());
        }
    }

    private Map<String, String> params;

    public Crawler params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public void post() {
        if (this.credential != null) {

        } else {
            crawl(new BasicHttpPost(this.params));
        }
    }

    private void crawl(HttpRequest httpRequest) {
        if (this.options == null) {
            this.options = Options.opts();
        }
        Fetcher fetcher = Fetcher.custom(this.options)
                .provider(httpRequest.getCredsProvider())
                .build();
        if (!RobotsCache.hasLocation(this.url.getOrigin())) {
            URL url = this.url.getUrl();
            Robots.custom(this.options).fetch(url.getScheme() + "://" + url.getLocation());
        }
        if (RobotsCache.hasLocation(this.url.getOrigin())) {
            return;
        }
        httpRequest.setWebURL(this.url);
        HttpEntity entity = fetcher.fetch(httpRequest).getEntity();
        if (entity != null) {
            this.handler.handle(entity, this.url);
        }
        fetcher.shutdown();
    }
}
