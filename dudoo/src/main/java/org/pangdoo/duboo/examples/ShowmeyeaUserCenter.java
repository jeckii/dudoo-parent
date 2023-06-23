package org.pangdoo.duboo.examples;

import org.jsoup.nodes.Document;
import org.pangdoo.duboo.crawler.Crawler;
import org.pangdoo.duboo.http.authentication.Credential;
import org.pangdoo.duboo.http.authentication.FormCredential;
import org.pangdoo.duboo.processor.page.PageHandler;
import org.pangdoo.duboo.url.WebURL;

import java.io.IOException;

public class ShowmeyeaUserCenter {

    public static void main(String[] args) {
        Credential credential = FormCredential
                .custom ("guest", "122345678")
                .loginUrl("http://www.showmeyea.com/app/smy/api/user/login")
                .usernameKey("account")
                .passwordKey("password");
        Crawler.custom()
                .credential(credential)
                .url("http://www.showmeyea.com/#/center")
                .process((entity, obj) -> {
                    try {
                        Document document = PageHandler.getDocument(entity.getContent(), "UTF-8", ((WebURL)obj).getLocation());
                        System.out.println("================================");
                        System.out.println("【Show Me Yea】");
                        System.out.printf("================================\n%s", document.outerHtml());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .get();
    }
}
