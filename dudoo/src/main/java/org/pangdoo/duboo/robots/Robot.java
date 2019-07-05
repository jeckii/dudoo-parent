package org.pangdoo.duboo.robots;

import java.util.List;

public class Robot {
	
	private List<String> disallow;
	
	private List<String> allow;

	public List<String> getDisallow() {
		return disallow;
	}

	public void setDisallow(List<String> disallow) {
		this.disallow = disallow;
	}

	public List<String> getAllow() {
		return allow;
	}

	public void setAllow(List<String> allow) {
		this.allow = allow;
	}

}
