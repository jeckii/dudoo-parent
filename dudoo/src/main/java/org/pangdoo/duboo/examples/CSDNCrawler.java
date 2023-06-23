package org.pangdoo.duboo.examples;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pangdoo.duboo.crawler.Crawler;
import org.pangdoo.duboo.fetcher.Options;
import org.pangdoo.duboo.processor.page.PageHandler;
import org.pangdoo.duboo.url.URLCollector;
import org.pangdoo.duboo.url.WebURL;
import org.pangdoo.duboo.url.collector.DefaultURLCollector;

import java.io.IOException;

public class CSDNCrawler {

    public static void main(String[] args) {
        Options options = Options.opts()
                .setGzip(true)
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
        URLCollector collector = DefaultURLCollector.custom()
                .url("https://blog.csdn.net/Hydrop");
        Crawler.custom()
                .collector(collector)
                .process((entity, obj) -> {
                    if (entity.isChunked() || entity.getContentLength() > 0) {
                        try {
                            Document document = PageHandler.getDocument(entity.getContent(), "UTF-8", ((WebURL)obj).getLocation());
                            Elements items = document.select(".article-item-box > .content > a[href^=https://blog.csdn.net/Hydrop]");
                            collector.addAll(items.eachAttr("href"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .options(options)
                .get();
    }
}
