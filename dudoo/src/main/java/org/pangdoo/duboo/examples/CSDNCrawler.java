package org.pangdoo.duboo.examples;

import org.apache.http.HttpEntity;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pangdoo.duboo.crawler.Crawler;
import org.pangdoo.duboo.fetcher.Options;
import org.pangdoo.duboo.processor.Processor;
import org.pangdoo.duboo.processor.page.PageHandler;
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
        collector.add("https://blog.csdn.net/Hydrop");
        Crawler.custom()
                .collector(collector)
                .process(new Processor() {

                    @Override
                    public void process(HttpEntity entity, Object obj) {
                        if (entity.isChunked() || entity.getContentLength() > 0) {
                            try {
                                Document document = PageHandler.getDocument(entity.getContent(), "UTF-8", ((WebURL)obj).getLocation());
                                Elements items = document.select(".article-item-box > .content > a[href^=https://blog.csdn.net/Hydrop]");
                                collector.addAll(items.eachAttr("href"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .options(options)
                .get();
    }
}
