package org.pangdoo.duboo.examples;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.pangdoo.duboo.crawler.Crawler;
import org.pangdoo.duboo.fetcher.Configuration;
import org.pangdoo.duboo.fetcher.Options;
import org.pangdoo.duboo.handler.Handler;
import org.pangdoo.duboo.handler.page.PageParser;
import org.pangdoo.duboo.http.HttpRequest;
import org.pangdoo.duboo.http.basic.BasicHttpPost;
import org.pangdoo.duboo.robots.RobotsParser;
import org.pangdoo.duboo.url.WebURL;

public class ShowmeyeaHostPost {
	
	public static void main(String[] args) throws Exception {


		Map<String, String> param = new HashMap<String, String>();
		param.put("hotspot", "川普誓言改变美国");
		Options options = Options.opts();
		Crawler.custom().url("http://www.showmeyea.com/app/smy/api/hotspot/update").handler(new Handler() {
			@Override
			public Object handle(HttpEntity entity, Object obj) {
				if (entity.isChunked() || entity.getContentLength() > 0) {
					try {
						RobotsParser parser = new RobotsParser(entity.getContent(), "UTF-8");
						System.out.println(parser.getContent());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		}).options(options).params(param).post();
	}

}
