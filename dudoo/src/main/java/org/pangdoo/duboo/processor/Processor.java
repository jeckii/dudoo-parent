package org.pangdoo.duboo.processor;

import org.apache.http.HttpEntity;

@FunctionalInterface
public interface Processor {

    void process(HttpEntity entity, Object obj);

}
