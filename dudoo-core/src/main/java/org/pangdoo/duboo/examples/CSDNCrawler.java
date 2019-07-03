package org.pangdoo.duboo.examples;

import org.apache.http.HttpEntity;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pangdoo.duboo.crawler.Crawler;
import org.pangdoo.duboo.fetcher.Options;
import org.pangdoo.duboo.handler.Handler;
import org.pangdoo.duboo.handler.page.PageParser;
import org.pangdoo.duboo.http.HttpResponse;
import org.pangdoo.duboo.url.URLCollector;
import org.pangdoo.duboo.url.WebURL;
import org.pangdoo.duboo.url.collector.DefaultURLCollector;

import java.io.IOException;

public class CSDNCrawler {

    public static void main(String[] args) {
        Options options = Options.opts();
        options.setGzip(true);
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
        options.setUserAgent(userAgent);
        URLCollector collector = new DefaultURLCollector();
        Crawler.custom().url("https://blog.csdn.net/Hydrop").handler(new Handler() {

            @Override
            public Object handle(HttpEntity entity, Object obj) {
                if (entity.isChunked() || entity.getContentLength() > 0) {
                    try {
                        Document document = PageParser.newInstance(entity.getContent(), "UTF-8", ((WebURL)obj).getLocation()).getDocument();
                        Elements items = document.select(".article-item-box > .content > a[href^=https://blog.csdn.net/Hydrop]");
                        collector.addAll(items.eachAttr("href"), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }).options(options).get();
        Crawler crawler = Crawler.custom().options(options);
        while (collector.hasNext()) {
            crawler.url(collector.consume().getOrigin()).get();
        }
    }
}
