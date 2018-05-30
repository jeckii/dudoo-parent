package org.pangdoo.duboo.handler;

import org.jsoup.nodes.Document;

public interface PageParser {
	
	Object parse(Document document);
	
}
