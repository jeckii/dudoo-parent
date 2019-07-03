package org.pangdoo.duboo.handler;

import org.apache.http.HttpEntity;
import org.pangdoo.duboo.http.HttpResponse;

public interface Handler {

    Object handle(HttpEntity entity, Object obj);

}
