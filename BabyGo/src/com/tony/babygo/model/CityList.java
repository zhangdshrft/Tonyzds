package com.tony.babygo.model;

import java.io.Serializable;
import java.util.List;

public class CityList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String status;
	
	private List<String> cities;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getCities() {
		return cities;
	}

	public void setCities(List<String> cities) {
		this.cities = cities;
	}
}
