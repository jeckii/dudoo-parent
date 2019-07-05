package org.pangdoo.duboo.examples;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.crawler.Crawler;
import org.pangdoo.duboo.fetcher.Options;
import org.pangdoo.duboo.processor.Processor;
import org.pangdoo.duboo.robots.RobotsParser;

public class ShowmeyeaHostPost {
	
	public static void main(String[] args) {

		Options options = Options.opts();
		Crawler.custom().url("http://www.showmeyea.com/app/smy/api/hotspot/update")
				.process(new Processor() {
					@Override
					public void process(HttpEntity entity, Object obj) {
						if (entity.isChunked() || entity.getContentLength() > 0) {
							try {
								RobotsParser parser = new RobotsParser(entity.getContent(), "UTF-8");
								System.out.println(parser.getContent());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				})
				.options(options)
				.param("hotspot", "川普誓言改变美国")
				.post();
	}

}
