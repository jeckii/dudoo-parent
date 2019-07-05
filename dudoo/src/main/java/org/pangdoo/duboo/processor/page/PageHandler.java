package org.pangdoo.duboo.processor.page;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.pangdoo.duboo.util.LogLogger;

public class PageHandler {
	
	private final static LogLogger logger = LogLogger.getLogger(PageHandler.class);
	
	public static Document getDocument(InputStream input, String charsetName, String baseUri) {
		try {
			if (input == null) {
				throw new PageIOException("Input stream is null.");
			}
		} catch (PageIOException e) {
			logger.warn(e);
		}
		try {
			return Jsoup.parse(input, charsetName, baseUri);
		} catch (IOException e) {
			logger.warn(e);
		}
		return null;
	}

}
