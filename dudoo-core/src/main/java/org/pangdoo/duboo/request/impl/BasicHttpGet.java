package org.pangdoo.duboo.request.impl;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.pangdoo.duboo.exception.NullValueException;
import org.pangdoo.duboo.request.AbstractUrlRequst;
import org.pangdoo.duboo.util.StringUtils;

public class BasicHttpGet extends AbstractUrlRequst {
	
	public BasicHttpGet() {
		super();
	}
	
	public BasicHttpGet(String url) {
		super(url);
	}

	@Override
	public HttpUriRequest request() throws NullValueException {
		if (StringUtils.isEmpty(this.url)) {
			throw new NullValueException("Url is null.");
		}
		return new HttpGet(this.url);
	}

}
