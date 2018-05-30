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

public class MeipianCrawler {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		Configuration configuration = new Configuration();
		List<String> urls = new ArrayList<String>();
		urls.add("https://www.meipian.cn/1byzft4d?share_from=self&utm_source=singlemessage&from=singlemessage&v=4.4.1&user_id=19541258&uuid=b471e9ab607f380a5a9e3d2e81e12bfe&utm_medium=meipian_android");
		PageCrawler crawler = new PageCrawler(configuration, urls, new PageParser() {

			@Override
			public Object parse(Document document) {
				Elements elements = document.getElementsByClass("img-box well img-box-bg offline-preview");
				List<String> hrefList = new ArrayList<String>();
				elements.forEach(ele -> {
					String href = ele.getElementsByTag("img").attr("src");
					hrefList.add(href);
				});
				return hrefList;
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
							"E:/java/files/Meipian/" + name.replaceAll(":", "%3A").replaceAll("/", "%2F").replaceAll(" ", "+"));
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
