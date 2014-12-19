package com.tony.babygo.model;

import java.io.Serializable;

public class Deal implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String description;
	
	private String url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
