package org.pangdoo.duboo.request;

import org.apache.http.client.methods.HttpUriRequest;

public abstract class AbstractUrlRequst {
	
	protected String url;
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public AbstractUrlRequst(String url) {
		this.url = url;
	}
	
	public AbstractUrlRequst() {
	}
	
	public abstract HttpUriRequest request();

}
