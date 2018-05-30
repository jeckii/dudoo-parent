package org.pangdoo.duboo.request.impl;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.pangdoo.duboo.request.AbstractUrlRequst;

public class BasicHttpGet extends AbstractUrlRequst {
	
	public BasicHttpGet() {
		super();
	}
	
	public BasicHttpGet(String url) {
		super(url);
	}

	@Override
	public HttpUriRequest request() {
		return new HttpGet(this.url);
	}

}
