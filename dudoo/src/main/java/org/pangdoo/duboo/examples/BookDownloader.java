package org.pangdoo.duboo.examples;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.crawler.Crawler;
import org.pangdoo.duboo.fetcher.Options;
import org.pangdoo.duboo.processor.Processor;
import org.pangdoo.duboo.processor.multi.MultiFileDownloader;

import java.io.IOException;

public class BookDownloader {
    public static void main(String[] args) {
        Options options = Options.opts();
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
        options.setUserAgent(userAgent);
        Crawler.custom()
                .url("http://pic.netbian.com/uploads/allimg/180331/182615-15224919751fac.jpg")
                .process(new Processor() {
                    @Override
                    public void process(HttpEntity entity, Object obj) {
                        if (entity.isChunked() || entity.getContentLength() > 0) {
                            MultiFileDownloader downloader = MultiFileDownloader.downloader("/home/jeckii/Pictures", "dav.jpg");
                            try {
                                downloader.download(entity.getContent());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                downloader.shutdown();
                            }
                        }
                    }
                })
                .options(options).get();
    }
}
