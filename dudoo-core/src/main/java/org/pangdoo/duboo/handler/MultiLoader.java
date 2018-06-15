package org.pangdoo.duboo.handler;

import org.apache.http.HttpEntity;

public interface MultiLoader {
	
	void load(HttpEntity entity, String fileName);

}
