package org.pangdoo.duboo.examples;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.crawler.executor.PageCrawlerExecutor;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.handler.PageParser;
import org.pangdoo.duboo.http.HttpRequest;
import org.pangdoo.duboo.http.basic.BasicHttpPost;
import org.pangdoo.duboo.robots.RobotstxtParser;
import org.pangdoo.duboo.url.WebUrl;

public class ShowmeyeaHostPost {
	
	public static void main(String[] args) throws Exception {
		Configuration config = new Configuration();
		PageCrawlerExecutor crawler = new PageCrawlerExecutor();
		Map<String, String> param = new HashMap<String, String>();
		param.put("hotspot", "川普誓言改变美国");
		String url = "http://www.showmeyea.com/app/smy/api/hotspot/update";
		HttpRequest request = new BasicHttpPost(param, new WebUrl(url));
		for (int i = 0;i < 500; i ++) {
			crawler.run(config, request, new PageParser() {
				
				@Override
				public Object parse(HttpEntity entity, String baseUrl) {
					try {
						RobotstxtParser parser = new RobotstxtParser(entity.getContent(), config.getCharset());
						System.out.println(parser.getContent());
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			});
		}
		crawler.shutdown();
	}

}
