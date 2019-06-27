package org.pangdoo.duboo.handler;

import org.apache.http.HttpEntity;

import java.io.IOException;

public interface Handler {

    Object handle(HttpEntity entity, Object obj);

}
