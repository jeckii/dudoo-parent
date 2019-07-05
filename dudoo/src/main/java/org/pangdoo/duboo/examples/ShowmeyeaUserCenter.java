package org.pangdoo.duboo.examples;

import org.apache.http.HttpEntity;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pangdoo.duboo.crawler.Crawler;
import org.pangdoo.duboo.http.authentication.Credential;
import org.pangdoo.duboo.http.authentication.FormCredential;
import org.pangdoo.duboo.processor.Processor;
import org.pangdoo.duboo.processor.page.PageHandler;
import org.pangdoo.duboo.url.WebURL;

import java.io.IOException;

public class ShowmeyeaUserCenter {

    public static void main(String[] args) {
        Credential credential
                = new FormCredential("guest", "122345678", "http://www.showmeyea.com/app/smy/api/user/login", "account","password");
        Crawler.custom()
                .credential(credential)
                .url("http://www.showmeyea.com/#/center")
                .process(new Processor() {
                    @Override
                    public void process(HttpEntity entity, Object obj) {
                        try {
                            Document document = PageHandler.getDocument(entity.getContent(), "UTF-8", ((WebURL)obj).getLocation());
                            System.out.println("================================");
                            System.out.println("【Show Me Yea】");
                            System.out.printf("================================\n%s", document.outerHtml());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .get();
    }
}
