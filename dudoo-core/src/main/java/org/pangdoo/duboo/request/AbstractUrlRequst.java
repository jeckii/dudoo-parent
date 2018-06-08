package org.pangdoo.duboo.request;

import org.apache.http.client.methods.HttpUriRequest;
import org.pangdoo.duboo.exception.NullValueException;

public abstract class AbstractUrlRequst {
	
	protected String url;
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public AbstractUrlRequst(String url) {
		this.url = url;
	}
	
	public AbstractUrlRequst() {
	}
	
	public abstract HttpUriRequest request() throws NullValueException;

}
