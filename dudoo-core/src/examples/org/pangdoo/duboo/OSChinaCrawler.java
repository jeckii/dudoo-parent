package org.pangdoo.duboo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pangdoo.duboo.crawler.PageCrawler;
import org.pangdoo.duboo.crawler.executor.PageCrawlerExecutor;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.handler.PageParser;
import org.pangdoo.duboo.handler.reader.HTMLReader;
import org.pangdoo.duboo.request.impl.BasicHttpGet;
import org.pangdoo.duboo.url.UrlCollector;
import org.pangdoo.duboo.url.collector.DefaultUrlCollector;

public class OSChinaCrawler {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		Configuration configuration = new Configuration();
		List<String> urls = new ArrayList<String>();
		urls.add("https://www.oschina.net/blog?classification=5593654");
		PageCrawler crawler = new PageCrawler(configuration, new PageParser() {

			@Override
			public Object parse(HttpEntity entity, String baseUrl) {
				List<String> hrefList = new ArrayList<String>();
				try {
					Document document = HTMLReader.newInstance(entity.getContent(), "UTF-8", baseUrl).getDocument();
					Elements elements = document.getElementsByClass("box item");
					elements.forEach(ele -> {
						String href = ele.getElementsByClass("sc overh blog-title-link").attr("href");
						hrefList.add(href);
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				return hrefList;
			}

		});
		List<Object> hrefs = crawler.crawl(new BasicHttpGet(), new DefaultUrlCollector(urls));

		class Blog {
			private String title;

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

		}

		PageCrawlerExecutor executor = new PageCrawlerExecutor();
		for (Object obj : hrefs) {
			List<String> href = (List<String>) obj;
			href = href.stream().filter(a -> a.startsWith("https://my.oschina.net")).collect(Collectors.toList());
			UrlCollector collector = new DefaultUrlCollector(href);
			while (collector.hasNext()) {
				Blog blog = (Blog) executor.run(configuration, new BasicHttpGet(collector.consume()), new PageParser() {

					@Override
					public Object parse(HttpEntity entity, String baseUrl) {
						FileOutputStream fos = null;
						String title = null;
						try {
							Document document = HTMLReader.newInstance(entity.getContent(), "UTF-8", baseUrl).getDocument();
							title = document.getElementsByClass("blog-heading").get(0).getElementsByClass("title")
									.text();
							String content = document.getElementById("blogBody").getElementsByClass("BlogContent clearfix")
									.outerHtml();
							File file = new File(
									args[0] + title.replaceAll(":", "%3A").replaceAll("/", "%2F").replaceAll(" ", "+") + ".html");
							fos = new FileOutputStream(file);
							fos.write(content.getBytes());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (fos != null) {
									fos.close();
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						Blog blog = new Blog();
						blog.setTitle(title);
						return blog;
					}

				});
				System.out.println("----------------------");
				if (blog != null) {
					System.out.println(blog.getTitle());
				}
			}
		}
		executor.shutdown();
	}

}
