package org.pangdoo.duboo.http.authentication;

import java.util.Map;

import org.pangdoo.duboo.http.basic.BasicHttpPost;
import org.pangdoo.duboo.url.WebURL;

public class AuthHttpPost extends BasicHttpPost {
	
	public AuthHttpPost(Credential credential, Map<String, String> params) {
		this(credential, params, null);
	}

	public AuthHttpPost(Credential credential, Map<String, String> params, WebURL webUrl) {
		super(params, webUrl);
		super.credential = credential;
	}

}
