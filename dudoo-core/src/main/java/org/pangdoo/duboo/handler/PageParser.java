package org.pangdoo.duboo.handler;

import org.apache.http.HttpEntity;

public interface PageParser {
	
	Object parse(HttpEntity entity, String baseUrl);
	
}
