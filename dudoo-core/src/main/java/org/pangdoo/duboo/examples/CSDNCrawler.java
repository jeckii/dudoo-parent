package org.pangdoo.duboo.examples;

import org.apache.http.HttpEntity;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.pangdoo.duboo.crawler.Crawler;
import org.pangdoo.duboo.fetcher.Options;
import org.pangdoo.duboo.handler.Handler;
import org.pangdoo.duboo.handler.page.PageParser;
import org.pangdoo.duboo.url.WebURL;

import java.io.IOException;

public class CSDNCrawler {

    public static void main(String[] args) {
        Options options = Options.opts();
        options.config().setGzip(true);
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
        options.config().setUserAgent(userAgent);
        Crawler.custom().url("https://blog.csdn.net/Hydrop").handler(new Handler() {
            @Override
            public Object handle(HttpEntity entity, Object obj) {
                if (entity.isChunked() || entity.getContentLength() > 0) {
                    try {
                        Document document = PageParser.newInstance(entity.getContent(), "UTF-8", ((WebURL)obj).getUrl().getLocation()).getDocument();
                        Element body = document.body();
                        System.out.println(body.outerHtml());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }).options(options).get();
    }
}
