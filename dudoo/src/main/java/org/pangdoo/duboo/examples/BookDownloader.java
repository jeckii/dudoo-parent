package org.pangdoo.duboo.examples;

import org.pangdoo.duboo.crawler.MultiFileCrawler;
import org.pangdoo.duboo.fetcher.Options;

public class BookDownloader {
    public static void main(String[] args) {
        Options options = Options.opts()
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
        MultiFileCrawler.custom()
                .url("http://pic.netbian.com/uploads/allimg/180331/182615-15224919751fac.jpg")
                .path("C://Users//jecki//Pictures", "dav.jpg")
                .options(options)
                .get()
                .download();
    }
}
