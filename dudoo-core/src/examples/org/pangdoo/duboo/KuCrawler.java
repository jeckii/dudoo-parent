package org.pangdoo.duboo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pangdoo.duboo.crawler.PageCrawler;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.crawler.MultiCrawler;
import org.pangdoo.duboo.handler.MultiLoader;
import org.pangdoo.duboo.handler.PageParser;
import org.pangdoo.duboo.request.impl.BasicHttpGet;

public class KuCrawler {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		Configuration configuration = new Configuration();
		List<String> urls = new ArrayList<String>();
		urls.add("http://588ku.com/video/1-0-3-0-pr/");
		PageCrawler crawler = new PageCrawler(configuration, urls, new PageParser() {

			@Override
			public Object parse(Document document) {
				Elements elements = document.getElementsByTag("video");
				List<String> href = new ArrayList<String>();
				elements.forEach(e -> {
					href.add(e.attr("src"));
				});
				return href;
			}

		});
		List<Object> hrefs = crawler.crawl(new BasicHttpGet(), 1000);
		MultiCrawler multiCrwaler = null;
		for (Object obj : hrefs) {
			List<String> href = (List<String>) obj;
			multiCrwaler = new MultiCrawler(configuration, href, new MultiLoader() {
				
				@Override
				public void load(InputStream input, String name) {
					File file = new File(
							"E:/java/files/Ku/" + name.replaceAll(":", "%3A").replaceAll("/", "%2F").replaceAll(" ", "+"));
					try {
						FileOutputStream fos = new FileOutputStream(file);
						byte[] bytes = new byte[1024];
						int off = -1;
						while ((off = input.read(bytes)) != -1) {
							fos.write(bytes, 0 ,off);
						}
						fos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			multiCrwaler.crawl(new BasicHttpGet(), 1000);
		}
	}

}
