package org.pangdoo.duboo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pangdoo.duboo.crawler.PageCrawler;
import org.pangdoo.duboo.crawler.executor.MultiCrawlerExecutor;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.handler.MultiLoader;
import org.pangdoo.duboo.handler.PageParser;
import org.pangdoo.duboo.request.impl.BasicHttpGet;
import org.pangdoo.duboo.url.UrlCollector;
import org.pangdoo.duboo.url.collector.DefaultUrlCollector;

public class MeipianCrawler {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		System.out.println("Start...");
		long startTime = System.currentTimeMillis();
		
		Configuration configuration = new Configuration();
		List<String> urls = new ArrayList<String>();
//		urls.add("https://www.meipian.cn/c/6925148");
		urls.add("https://www.meipian.cn/c/19541258");
		PageCrawler urlCrawler = new PageCrawler(configuration, new PageParser() {

			@Override
			public Object parse(Document document) {
				Elements elements = document.getElementsByClass("articleinfo");
				List<String> hrefList = new ArrayList<String>();
				elements.forEach(ele -> {
					String href = ele.getElementsByTag("h3").get(0).getElementsByTag("a").attr("href");
					hrefList.add(href);
				});
				return hrefList;
			}

		});
		List<Object> actions = urlCrawler.crawl(new BasicHttpGet(), new DefaultUrlCollector(urls));
		
		System.out.println("Fetch first...");
		long fetchFirst = System.currentTimeMillis();
		System.out.println(fetchFirst - startTime);
		
		List<String> actionUrls = new ArrayList<String>();
		actions.forEach(o -> {
			List<String> href = (List<String>) o;
			href.forEach(h -> {
				actionUrls.add(h);
			});
		});
//		urls.add("https://www.meipian.cn/1byzft4d?share_from=self&utm_source=singlemessage&from=singlemessage&v=4.4.1&user_id=19541258&uuid=b471e9ab607f380a5a9e3d2e81e12bfe&utm_medium=meipian_android");
/*		urls.add("https://www.meipian.cn/1c21l4fk");
		urls.add("https://www.meipian.cn/1arl8bvy");*/
		PageCrawler crawler = new PageCrawler(configuration, new PageParser() {

			@Override
			public Object parse(Document document) {
				Elements elements = document.getElementsByClass("img-box");
				List<String> hrefList = new ArrayList<String>();
				elements.forEach(ele -> {
					String href = ele.getElementsByTag("img").attr("src");
					hrefList.add(href);
				});
				return hrefList;
			}

		});
		
		System.out.println(actionUrls.size() + " pages need to fetch.");
		List<Object> hrefs = crawler.crawl(new BasicHttpGet(), new DefaultUrlCollector(actionUrls));
		
		System.out.println("Fetch second...");
		long fetchSecond = System.currentTimeMillis();
		System.out.println(fetchSecond - startTime);
		
		MultiCrawlerExecutor executor = new MultiCrawlerExecutor();
		for (Object obj : hrefs) {
			List<String> href = (List<String>) obj;
			UrlCollector collector = new DefaultUrlCollector(href);
			while (collector.hasNext()) {
				executor.run(configuration, collector.consume(), new BasicHttpGet(), new MultiLoader() {
					
					@Override
					public void load(InputStream input, String name) {
						name = name.replaceAll("-mobile", "");
						File file = new File(
								"E:/java/files/Meipian/bike/" + name.replaceAll(":", "%3A").replaceAll("/", "%2F").replaceAll(" ", "+"));
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
			}
		}
		executor.shutdown();
		
		System.out.println("End...");
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
	}

}
