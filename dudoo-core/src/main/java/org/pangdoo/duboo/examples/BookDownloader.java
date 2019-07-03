package org.pangdoo.duboo.examples;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.crawler.Crawler;
import org.pangdoo.duboo.fetcher.Options;
import org.pangdoo.duboo.handler.Handler;
import org.pangdoo.duboo.handler.writer.OutputStreamWriter;

import java.io.IOException;

public class BookDownloader {
    public static void main(String[] args) {
        Options options = Options.opts();
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
        options.setUserAgent(userAgent);
        Crawler.custom().url("http://pic.netbian.com/uploads/allimg/180331/182615-15224919751fac.jpg").handler(new Handler() {
            @Override
            public Object handle(HttpEntity entity, Object obj) {
                if (entity.isChunked() || entity.getContentLength() > 0) {
                    OutputStreamWriter writer = OutputStreamWriter.newInstance("/home/jeckii/Pictures", "dav.jpg");
                    try {
                        writer.write(entity.getContent());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }).options(options).get();
    }
}
