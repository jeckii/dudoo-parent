package org.pangdoo.duboo.crawler;

import org.pangdoo.duboo.fetcher.Options;
import org.pangdoo.duboo.http.authentication.Credential;
import org.pangdoo.duboo.processor.Processor;
import org.pangdoo.duboo.processor.multi.MultiFileDownloader;
import org.pangdoo.duboo.url.URLCollector;
import org.pangdoo.duboo.util.LogLogger;

import java.io.IOException;
import java.util.Map;

public class MultiFileCrawler {

    private final static LogLogger logger = LogLogger.getLogger(MultiFileCrawler.class);

    private Crawler crawler;

    private HTTP_METHOD httpMethod;

    enum HTTP_METHOD {
        HTTP_POST, HTTP_GET
    }

    private MultiFileCrawler() {
    }

    public static MultiFileCrawler custom() {
        MultiFileCrawler downloader = new MultiFileCrawler();
        downloader.crawler = Crawler.custom();
        return downloader;
    }

    public MultiFileCrawler options(Options options) {
        this.crawler.options(options);
        return this;
    }

    public MultiFileCrawler url(String url) {
        this.crawler.url(url);
        return this;
    }

    public MultiFileCrawler collector(URLCollector urlCollector) {
        this.crawler.collector(urlCollector);
        return this;
    }

    public MultiFileCrawler credential(Credential credential) {
        this.crawler.credential(credential);
        return this;
    }

    public MultiFileCrawler params(Map<String, String> params) {
        this.crawler.params(params);
        return this;
    }

    public MultiFileCrawler param(String param, String value) {
        this.crawler.param(param, value);
        return this;
    }

    public MultiFileCrawler path(String path, String fileName) {
        this.crawler.process((entity, obj) -> {
            if (entity.isChunked() || entity.getContentLength() > 0) {
                MultiFileDownloader downloader = MultiFileDownloader.downloader(path, fileName);
                try {
                    downloader.download(entity.getContent());
                } catch (IOException e) {
                    logger.error("Download files error", e);
                } finally {
                    downloader.shutdown();
                }
            }
        });
        return this;
    }

    public MultiFileCrawler path(String path) {
        this.crawler.process((entity, obj) -> {
            if (entity.isChunked() || entity.getContentLength() > 0) {
                MultiFileDownloader downloader = MultiFileDownloader.downloader(path);
                try {
                    downloader.download(entity.getContent());
                } catch (IOException e) {
                    logger.error("Download files error", e);
                } finally {
                    downloader.shutdown();
                }
            }
        });
        return this;
    }

    public MultiFileCrawler process(Processor processor) {
        this.crawler.process(processor);
        return this;
    }

    public MultiFileCrawler post() {
        this.httpMethod = HTTP_METHOD.HTTP_POST;
        return this;
    }

    public MultiFileCrawler get() {
        this.httpMethod = HTTP_METHOD.HTTP_GET;
        return this;
    }

    public void download() {
        if (this.httpMethod == null) {
            this.httpMethod = HTTP_METHOD.HTTP_GET;
        }
        if (HTTP_METHOD.HTTP_GET == this.httpMethod) {
            this.crawler.get();
        } else {
            this.crawler.post();
        }
    }

}
